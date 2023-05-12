package com.github.supernevi.karaokeprovider;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.Architectures.LayeredArchitecture;

@AnalyzeClasses(packages = "com.github.supernevi.karaokeprovider")
public class ArchitecturalTest {
	
	@ArchTest
	private final ArchRule serviceNaming = classes()
		.that()
			.areAnnotatedWith(Service.class)
		.should().haveSimpleNameEndingWith("ServiceImpl")
		.andShould().resideInAnyPackage("..services")
		.andShould(haveNoMemberVariablesButServiceAndDao());

	@ArchTest
	private final ArchRule daoNaming = classes().that().areAnnotatedWith(Repository.class)
		.should().haveSimpleNameEndingWith("DaoImpl")
		.andShould().resideInAnyPackage("..daos")
		.andShould(haveNoMemberVariablesButLogger());

	@ArchTest
	private final ArchRule daoMethodReturn = methods().that().areDeclaredInClassesThat().areAnnotatedWith(Repository.class).and().arePublic()
	    .should(haveReturnTypeSimpleNameStartingWith("BO"))
	    .orShould(haveReturnTypeSimpleNameStartingWith("JO"))
	    .orShould(haveReturnTypeSimpleNameStartingWith("JBO"))
	    .orShould().haveRawReturnType(boolean.class)
	    .orShould().haveRawReturnType(int.class)
	    .orShould().haveRawReturnType(long.class)
	    .orShould().haveRawReturnType(void.class)
	    .orShould(haveReturnTypeBOCollection());
	
	@ArchTest
	private final ArchRule tos = classes().that().haveSimpleNameStartingWith("TO")
		.should().resideInAnyPackage("..entities.transfer");
	
	@ArchTest
	private final ArchRule restServices = classes().that().areAnnotatedWith(RestController.class)
		.should().resideInAnyPackage("..rest")
		.andShould().haveSimpleNameEndingWith("RestControllerImpl");
	
	@ArchTest
	private final LayeredArchitecture layers = layeredArchitecture()
		.consideringAllDependencies()
		.layer("API").definedBy("..rest..")
		.layer("Service").definedBy("..services..")
		.layer("Persistence").definedBy("..daos..")
		.whereLayer("API").mayNotBeAccessedByAnyLayer();
	

	private ArchCondition<JavaMethod> haveReturnTypeSimpleNameStartingWith(String prefix) {
		return new ArchCondition<JavaMethod>("have return type") {
			@Override
			public void check(JavaMethod item, ConditionEvents events) {
				String simpleName = item.reflect().getReturnType().getSimpleName();
				boolean matches = simpleName.startsWith(prefix);
				String message = String.format("method return type name of '%s' is not starting with '%s'", item.getFullName(), prefix);
				events.add(new SimpleConditionEvent(item, matches, message));
			}
		};
	}
	
	private ArchCondition<JavaMethod> haveReturnTypeBOCollection() {
		ArchCondition<JavaMethod> haveReturnTypeCollection = new ArchCondition<JavaMethod>("is Collection") {

			@Override
			public void check(JavaMethod item, ConditionEvents events) {
				boolean matches = true;
				JavaClass returnClass = item.getRawReturnType();
				if (!returnClass.reflect().isAssignableFrom(Collection.class)) {
					matches = false;
				}
				else {
					Type returnType = item.reflect().getGenericReturnType();
					if (returnType instanceof ParameterizedType) {
						ParameterizedType type = (ParameterizedType) returnType;
						Type[] typeArguments = type.getActualTypeArguments();
						for (Type typeArgument : typeArguments) {
							try {
								Class<?> typeArgClass = (Class<?>) typeArgument;
								String simpleClassName = typeArgClass.getSimpleName();
								if (!StringUtils.startsWith(simpleClassName, "BO") && !StringUtils.startsWith(simpleClassName, "JBO")
								                && !StringUtils.startsWith(simpleClassName, "JO") && !typeArgClass.equals(Long.class) && !typeArgClass.equals(String.class)) {
									matches = false;
								}
							}
							catch (ClassCastException e) {
								continue;
							}
						}
					}
				}

				String message = String.format("%s is not bo collection", item.getFullName());
				events.add(new SimpleConditionEvent(item, matches, message));

			}
		};
		return haveReturnTypeCollection;
	}
	
	private ArchCondition<JavaClass> haveNoMemberVariablesButLogger() {
		ArchCondition<JavaClass> haveNoMenberVariablesButLogger = new ArchCondition<JavaClass>("has no members") {

			@Override
			public void check(JavaClass item, ConditionEvents events) {
				boolean matches = true;
				Set<JavaField> memberVars = item.getFields();
				for (JavaField field : memberVars) {
					if (!field.getRawType().isAssignableFrom(Logger.class)) {
						boolean okay = Modifier.isStatic(field.reflect().getModifiers()) && Modifier.isFinal(field.reflect().getModifiers());
						if (!okay) {
							matches = false;
						}

					}
				}
				String message = String.format("%s has local members", item.getFullName());
				events.add(new SimpleConditionEvent(item, matches, message));

			}

		};
		return haveNoMenberVariablesButLogger;
	}
	
	
	private ArchCondition<JavaClass> haveNoMemberVariablesButServiceAndDao() {
		ArchCondition<JavaClass> haveNoMemberVariablesButLogger = new ArchCondition<JavaClass>("has no members") {

			@Override
			public void check(JavaClass item, ConditionEvents events) {
				try {
					Profile profileAnno = item.getAnnotationOfType(Profile.class);
					if (profileAnno != null) {
						for (String value : profileAnno.value()) {
							if (StringUtils.equals(value, "test")) {
								return;
							}
						}
					}
				}
				catch (IllegalArgumentException e) {
					// Ignore
				}
				try {
					RequestScope requestScope = item.getAnnotationOfType(RequestScope.class);
					if (requestScope != null) {
						return;
					}
				}
				catch (IllegalArgumentException e) {
					// Ignore
				}
				Set<String> foundMemberVars = new HashSet<>();
				
				for (JavaField field : item.getFields()) {
					if (!(StringUtils.endsWith(field.getRawType().getSimpleName(), "Service"))
							&& !(StringUtils.endsWith(field.getRawType().getSimpleName(), "Dao"))
					        && !StringUtils.startsWith(field.getName(), "$SWITCH_TABLE$") // ????
					) {
						boolean isStatic = Modifier.isStatic(field.reflect().getModifiers());
						boolean isFinal = Modifier.isFinal(field.reflect().getModifiers());
						if (!isFinal || !isStatic) {
							foundMemberVars.add(field.getName());
						}

					}
				}
				String message = String.format("%s has local members: %s", item.getFullName(), String.join(", ", foundMemberVars));
				events.add(new SimpleConditionEvent(item, foundMemberVars.isEmpty(), message));
			}

		};
		return haveNoMemberVariablesButLogger;
	}
}
