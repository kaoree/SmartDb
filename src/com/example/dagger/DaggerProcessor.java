package com.example.dagger;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.dagger.util.DataContainer;
import com.example.dagger.Id.Strategy;

import android.database.Cursor;
import android.util.Log;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DaggerProcessor extends AbstractProcessor {
	public Types typeUtils;
	public Filer filer;
	public static String DATABASE_FILE = "database_file";
	public List<String> test = null;
	public Elements elementUtils;
	private Map<String, Set<String>> OneOneforignColmun = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> ManyOneforignColmun = new HashMap<String, Set<String>>();
	private static String LIST_TYPE = List.class.getCanonicalName();
	private List<String> primaryType = Arrays.asList("Integer", "String",
			"Short", "Boolean", "Long", "Double", "Float", "Date");

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		// TODO Auto-generated method stub
		Set<String> supportedAnnotations = new LinkedHashSet<String>();
		supportedAnnotations.add(ForignColumn.class.getCanonicalName());
		supportedAnnotations.add(Id.class.getCanonicalName());
		supportedAnnotations.add(Column.class.getCanonicalName());
		supportedAnnotations.add(Table.class.getCanonicalName());
		return supportedAnnotations;
	}

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		// TODO Auto-generated method stub
		super.init(processingEnv);
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Kind.NOTE, "Printing: Processor is run");
		typeUtils = processingEnv.getTypeUtils();
		filer = processingEnv.getFiler();
		elementUtils = processingEnv.getElementUtils();

	}

	@Override
	protected synchronized boolean isInitialized() {
		// TODO Auto-generated method stub
		return super.isInitialized();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		// TODO Auto-generated method stub
		// MyModel model=new Test();
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Kind.WARNING, "Printing: Processor is run");
		HashMap<String, BindingClass> targetClassMap = new LinkedHashMap<String, BindingClass>();
		targetClassMap = (LinkedHashMap<String, BindingClass>) findAndParseTarget(roundEnv);
		if (targetClassMap.size() > 0) {
			DataContainer.Instance().setBindclassMap(targetClassMap);
		}
		JavaFileGenerator javaGenerator = new JavaFileGenerator();
		JavaFileObject jfo;
		try {
			jfo = filer.createSourceFile(javaGenerator.getFunc(), null);
			Writer writer = jfo.openWriter();
			writer.write(javaGenerator.brewJava(targetClassMap).toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// DataContainer.Instance().setBindclassMap(targetClassMap);
		if (targetClassMap.size() > 0) {
			DataContainer.Instance().writeDataToCache();
		}
		// try {
		// for (Map.Entry<String, BindingClass> binding : targetClassMap
		// .entrySet()) {
		// BindingClass bindingClass = binding.getValue();
		// // File dir = new
		// // File("C://"+binding.brewJava().toString()+".txt");
		// // if(!dir.exists()){
		// // dir.mkdir();
		// // }
		// JavaFileObject jfo = filer.createSourceFile(DATABASE_FILE
		// + bindingClass.getClassName(), null);
		// Writer writer = jfo.openWriter();
		// writer.write(bindingClass.brewJava().toString());
		// writer.flush();
		// writer.close();
		// }
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return true;
	}

	public Map<String, BindingClass> findAndParseTarget(RoundEnvironment env) {
		Map<String, BindingClass> targetClassMap = new LinkedHashMap<String, BindingClass>();
		for (Element element : env.getElementsAnnotatedWith(Id.class)) {
			parseId(element, targetClassMap);
		}
		for (Element element : env.getElementsAnnotatedWith(Column.class)) {
			parseColumn(element, targetClassMap);
		}
		for (Element element : env.getElementsAnnotatedWith(ForignColumn.class)) {
			parseForignColumn(element, targetClassMap);
		}
		for (Element element : env.getElementsAnnotatedWith(Table.class)) {
			parseTable(element, targetClassMap);
		}

		return targetClassMap;
	}

	private void parseId(Element element,
			Map<String, BindingClass> targetClassMap) {
		String className = element.getSimpleName().toString();
		TypeMirror primaryKeyType = element.asType();
		Strategy generateStrategy = element.getAnnotation(Id.class).strategy();
		if (generateStrategy == Strategy.AUTO) {
			parseAutoGenerateId(element, targetClassMap);
		} else if (generateStrategy == Strategy.UUID) {
			parseUUIDGenerateId(element, targetClassMap);
		}

	}

	private void parseAutoGenerateId(Element element,
			Map<String, BindingClass> targetClassMap) {
		TypeMirror fieldtype = null;
		TypeElement enclosingElement = (TypeElement) element
				.getEnclosingElement();
		fieldtype = element.asType();
		if (fieldtype.toString().equals("java.lang.Integer")
				|| fieldtype.toString().equals("int")) {
			String fieldname = element.getSimpleName().toString();
			String columnname = element.getAnnotation(Id.class).value();
			BindingClass bindingclass = getOrCreateTargetClass(targetClassMap,
					enclosingElement);
			if (bindingclass.getIdColumnBinding() == null) {
				IdColumnBinding idbinding = new IdColumnBinding(columnname,
						fieldtype.toString(), Strategy.AUTO);
				bindingclass.addIdColumnBinding(idbinding);

			} else {
				throw new IllegalArgumentException(
						"one class must have only one primary key");
			}
		} else {
			throw new IllegalArgumentException(
					" AutoCreate primary Key Type must be Integer or int");
		}
	}

	private void parseUUIDGenerateId(Element element,
			Map<String, BindingClass> targetClassMap) {
		TypeMirror fieldtype = null;
		TypeElement enclosingElement = (TypeElement) element
				.getEnclosingElement();
		fieldtype = element.asType();
		if (fieldtype.toString().equals("java.lang.String")) {
			String fieldname = element.getSimpleName().toString();
			String columnname = element.getAnnotation(Id.class).value();
			BindingClass bindingclass = getOrCreateTargetClass(targetClassMap,
					enclosingElement);
			if (bindingclass.getIdColumnBinding() == null) {
				IdColumnBinding idbinding = new IdColumnBinding(columnname,
						fieldtype.toString(), Strategy.UUID);
				bindingclass.addIdColumnBinding(idbinding);
			} else {
				throw new IllegalArgumentException(
						"one class must have only one primary key");
			}

		} else {
			throw new IllegalArgumentException(
					"  UUID primary Key Type must be String");
		}

	}

	private void parseForignColumn(Element element,
			Map<String, BindingClass> targetClassMap) {
		String className = element.getSimpleName().toString();
		TypeMirror forignType = element.asType();

		if (LIST_TYPE.equals(eraseListType(forignType))) {
			parseManyForignColumn(element, targetClassMap);
		} else {
			parseOneForignColumn(element, targetClassMap);
		}

	}

	public void parseManyForignColumn(Element element,
			Map<String, BindingClass> targetClassMap) {
		TypeMirror columnType = null;

		TypeElement enclosingElement = (TypeElement) element
				.getEnclosingElement();
		TypeElement innerElement = null;
		columnType = element.asType();

		DeclaredType declaredTpe = (DeclaredType) columnType;
		List<? extends TypeMirror> typeArguments = declaredTpe
				.getTypeArguments();
		if (declaredTpe.getTypeArguments().size() != 1) {
			throw new IllegalArgumentException("list typeArguments must be one");
		}
		columnType = typeArguments.get(0);
		if (columnType != null && columnType.getKind() == TypeKind.TYPEVAR) {
			TypeVariable temType = (TypeVariable) columnType;
			columnType = temType.getUpperBound();
		}

		if (!(columnType instanceof DeclaredType)) {
			throw new IllegalArgumentException("forignColumn must be class");
		} else if (isPrimaryType(columnType)) {
			throw new IllegalArgumentException(
					"forignColumn must not be primary type");
		} else {
			Element forignElement = ((DeclaredType) columnType).asElement();
			innerElement = (TypeElement) forignElement;

		}
		String fieldname = element.getSimpleName().toString();
		String columnname = element.getAnnotation(ForignColumn.class).value();
		BindingClass bindingclass = getOrCreateTargetClass(targetClassMap,
				enclosingElement);
		BindingClass innerbindingclass = getOrCreateTargetClass(targetClassMap,
				innerElement);
		String forignColumn = getOrCreateTargetClass(targetClassMap,
				enclosingElement).getIdColumnBinding().getName();
		Strategy strategy = getOrCreateTargetClass(targetClassMap,
				enclosingElement).getIdColumnBinding().getStratery();
		String forignColumnType = "";
		String forignType = "";
		if (strategy == Strategy.AUTO) {
			forignColumnType = "INTEGER";
			forignType = "int";
		} else if (strategy == Strategy.UUID) {
			forignColumnType = "TEXT";
			forignType = "java.lang.String";
		}
		if (getOrCreateTargetClass(targetClassMap, enclosingElement)
				.getIdColumnBinding().getName() == null) {

		}
		File dir = new File("C://" + columnType.toString() + "inner.txt");
		if (!dir.exists()) {
			dir.mkdir();
		}
		ForignColumnBinding forignColumnBinding = new ForignColumnBinding(
				columnname, fieldname, columnType.toString(), forignColumnType,
				forignType, forignColumn, enclosingElement.getSimpleName()
						.toString(), true);

		bindingclass.addForignColumn(forignColumnBinding);
		emitForignKeyColumnBinding(forignColumnBinding, innerbindingclass);

	}

	private void emitForignKeyColumnBinding(ForignColumnBinding binding,
			BindingClass bind) {
		// String co
		String columnName = binding.getName();
		// String fieldType = binding.getFieldType();
		String forignColumn = binding.getForignColumn();
		String forignColumnType = binding.getForignColumType();
		String referenceClass = binding.getReferenceClass();
		StringBuilder builder = bind.getStringBuilder();
		builder.append(",");
		builder.append(columnName).append("  ").append(forignColumnType)
				.append(",");
		builder.append("  foreign key");
		builder.append("(").append(columnName).append(") ");
		builder.append("references ").append(referenceClass).append("(")
				.append(forignColumn).append(")");

	}

	public void parseOneForignColumn(Element element,
			Map<String, BindingClass> targetClassMap) {
		TypeMirror columnType = null;
		TypeElement enclosingElement = (TypeElement) element
				.getEnclosingElement();
		TypeElement innerElement = null;
		columnType = element.asType();
		if (!(columnType instanceof DeclaredType)) {
			throw new RuntimeException("forignColumn must be class");
		} else if (isPrimaryType(columnType)) {
			throw new RuntimeException("forignColumn must not be primary type");
		}
		DeclaredType forignElement = (DeclaredType) columnType;
		innerElement = (TypeElement) forignElement.asElement();// 关联类元素
		String fieldname = element.getSimpleName().toString();// 字段名
		String columnname = element.getAnnotation(ForignColumn.class).value();// 关联类的外键列名
		BindingClass bindingclass = getOrCreateTargetClass(targetClassMap,
				enclosingElement);
		BindingClass innerbindingclass = getOrCreateTargetClass(targetClassMap,
				innerElement);
		String forignColumn = getOrCreateTargetClass(targetClassMap,
				enclosingElement).getIdColumnBinding().getName();// 类的主键字段名称
		Strategy strategy = getOrCreateTargetClass(targetClassMap,
				enclosingElement).getIdColumnBinding().getStratery();
		String forignColumnType = "";
		String forignType = "";
		if (strategy == Strategy.AUTO) {
			forignColumnType = "INTEGER";
			forignType = "int";
		} else if (strategy == Strategy.UUID) {
			forignColumnType = "TEXT";
			forignType = "java.lang.String";
		}
		ForignColumnBinding forignColumnBinding = new ForignColumnBinding(
				columnname, fieldname, columnType.toString(), forignColumnType,
				forignType, forignColumn, enclosingElement.getSimpleName()
						.toString(), false);
		bindingclass.addForignColumn(forignColumnBinding);
		emitForignKeyColumnBinding(forignColumnBinding, innerbindingclass);

	}

	public void parseTable(Element element,
			Map<String, BindingClass> targetClassMap) {
		String className = element.getSimpleName().toString();
		TypeMirror classType = element.asType();
		String tableName = element.getAnnotation(Table.class).value();
		BindingClass bindingclass = getOrCreateTargetClass(targetClassMap,
				(TypeElement) element);
		ClassTableBinding classbinding = new ClassTableBinding(className,
				tableName);
		bindingclass.addClassTable(classbinding);

	}

	public void parseColumn(Element element,
			Map<String, BindingClass> targetClassMap) {
		TypeMirror type = element.asType();
		if (LIST_TYPE.equals(eraseListType(type))) {
			parseMany(element, targetClassMap);
		} else {
			parseOne(element, targetClassMap);

		}

	}

	public void parseOne(Element element,
			Map<String, BindingClass> targetClassMap) {
		TypeMirror columnType = null;
		TypeElement enclosingElement = (TypeElement) element
				.getEnclosingElement();
		columnType = element.asType();
		String fieldname = element.getSimpleName().toString();
		String columnname = element.getAnnotation(Column.class).value();
		int sortIndex = element.getAnnotation(Column.class).sort();
		BindingClass bindingclass = getOrCreateTargetClass(targetClassMap,
				enclosingElement);
		FieldColumnBinding fieldbinding = new FieldColumnBinding(columnname,
				fieldname, columnType.toString(), sortIndex, false);

		bindingclass.addFieldColumn(sortIndex, fieldbinding);

	}

	public void parseMany(Element element,
			Map<String, BindingClass> targetClassMap) {
		TypeMirror columnType = null;
		TypeElement enclosingElement = (TypeElement) element
				.getEnclosingElement();
		TypeMirror elementType = element.asType();
		DeclaredType declaredTpe = (DeclaredType) elementType;
		List<? extends TypeMirror> typeArguments = declaredTpe
				.getTypeArguments();
		if (declaredTpe.getTypeArguments().size() != 1) {
			throw new IllegalArgumentException("list typeArguments must be one");
		}
		columnType = typeArguments.get(0);
		if (columnType != null && columnType.getKind() == TypeKind.TYPEVAR) {
			TypeVariable temType = (TypeVariable) columnType;
			columnType = temType.getUpperBound();
		}

		String fieldname = element.getSimpleName().toString();
		String columnname = element.getAnnotation(Column.class).value();
		int sortIndex = element.getAnnotation(Column.class).sort();
		BindingClass bindingclass = getOrCreateTargetClass(targetClassMap,
				enclosingElement);
		FieldColumnBinding fieldbinding = new FieldColumnBinding(columnname,
				fieldname, columnType.toString(), sortIndex, true);
		bindingclass.addFieldColumn(sortIndex, fieldbinding);
	}

	private void createManytoOneForignColumn(TypeMirror columnType,
			Element enclosingElement) {
		if (ManyOneforignColmun.containsKey(columnType.toString())) {
			Set<String> forignClasses = ManyOneforignColmun.get(columnType
					.toString());
			forignClasses.add(enclosingElement.getSimpleName().toString());
		} else {
			Set<String> forignClasses = new HashSet<String>();
			forignClasses.add(enclosingElement.getSimpleName().toString());
			ManyOneforignColmun.put(columnType.toString(), forignClasses);
		}

	}

	private void createOneToOneForignColumn(TypeMirror columnType,
			Element enclosingElement) {
		if (OneOneforignColmun.containsKey(columnType.toString())) {
			Set<String> forignClasses = OneOneforignColmun.get(columnType
					.toString());
			forignClasses.add(enclosingElement.getSimpleName().toString());
		} else {
			Set<String> forignClasses = new HashSet<String>();
			forignClasses.add(enclosingElement.getSimpleName().toString());
			OneOneforignColmun.put(columnType.toString(), forignClasses);
		}
	}

	// private boolean isForignColumn(TypeMirror columnType){
	//
	// if(columnType instanceof DeclaredType){
	// String name= ((DeclaredType)
	// columnType).asElement().getSimpleName().toString();
	// if(isPrimaryType(name)){
	// return false;
	// }
	// return true;
	// }
	// return false;
	//
	// }
	// private boolean isPrimaryType(TypeMirror type){
	//
	// if(primaryType.contains(typeName)){
	// return true;
	// }
	// return false;
	// }
	private String eraseListType(TypeMirror type) {
		String name = typeUtils.erasure(type).toString();
		int typeParamStart = name.indexOf('<');
		if (typeParamStart != -1) {
			name = name.substring(0, typeParamStart);
		}
		// File dir = new File("C://d1" +
		// List.class.getCanonicalName().toString()
		// + ".txt");
		// if (!dir.exists()) {
		// dir.mkdir();
		// }
		return name;
	}

	private BindingClass getOrCreateTargetClass(
			Map<String, BindingClass> targetMap, TypeElement enclosingElement) {
		BindingClass bindingclass = targetMap.get(enclosingElement
				.getSimpleName().toString());

		if (bindingclass == null) {
			String tagetType = enclosingElement.getQualifiedName().toString();
			String packageName = getPackageName(enclosingElement);
			String className = getClassName(enclosingElement, packageName);
			bindingclass = new BindingClass(tagetType, packageName, className);
			targetMap.put(enclosingElement.getSimpleName().toString(),
					bindingclass);

		}
		// File dir = new File("C://" + bindingclass.getClassName() + "df.txt");
		// if (!dir.exists()) {
		// dir.mkdir();
		// }
		return bindingclass;
	}

	private String getPackageName(TypeElement type) {
		return elementUtils.getPackageOf(type).getQualifiedName().toString();
	}

	private boolean isPrimaryType(TypeMirror type) {
		DeclaredType declaredType = (DeclaredType) type;
		TypeElement typeElement = (TypeElement) declaredType.asElement();
		String innerType = typeElement.getSimpleName().toString();
		return primaryType.contains(innerType);
	}

	private static String getClassName(TypeElement type, String packageName) {
		int packageLen = packageName.length() + 1;
		return type.getQualifiedName().toString().substring(packageLen)
				.replace('.', '$');
	}

}
