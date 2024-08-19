package ics.on_safety.desafio.crud.archtest;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.GeneralCodingRules;
import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "ics.on_safety.desafio.crud", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {


    @ArchTest
    public final ArchRule controllerTest = ArchRuleDefinition.classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..api..")
            .andShould().haveSimpleNameEndingWith("Controller")
            .andShould().haveSimpleNameNotEndingWith("RestController");

    @ArchTest
    public final ArchRule servicesTest = ArchRuleDefinition.classes()
            .that().resideInAPackage("..service..")
            .should().beAnnotatedWith(Service.class);

    @ArchTest
    public final ArchRule componentIsNotAllowed = ArchRuleDefinition.classes()
            .that().resideInAPackage("..service..")
            .should().notBeAnnotatedWith(Component.class)
            .because("Component Annotation is not allowed in package service");

    @ArchTest
    public final ArchRule controllerDoNotCallRepositoryTest = ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..api..")
            .should().dependOnClassesThat().resideInAPackage("..repository..")
            .as("Controller não pode chamar diretamente o repository.");

    @ArchTest
    public final ArchRule entityTest = ArchRuleDefinition.classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("..model..");

    @ArchTest
    public final ArchRule repositoryTest = ArchRuleDefinition.classes()
            .that().resideInAPackage("..repository..").should()
            .beAnnotatedWith(Repository.class)
            .because("Repository é uma classe/interface de persistência @Repository");

    @ArchTest
    public final ArchRule repositoryClassInterface = ArchRuleDefinition.classes()
            .that().resideInAPackage("..repository..")
            .should().beInterfaces();

    @ArchTest
    public final ArchRule fieldsEntity = ArchRuleDefinition.fields()
            .that().areDeclaredInClassesThat()
            .resideInAPackage("..model..")
            .should().bePrivate();

    @ArchTest
    public final ArchRule controllerMethodsTest = ArchRuleDefinition.noMethods()
            .that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should().beAnnotatedWith(RequestMapping.class);

    @ArchTest
    public final ArchRule logTest = ArchRuleDefinition.fields()
            .that().haveRawType(Logger.class)
            .should().bePrivate()
            .andShould().beStatic()
            .andShould().beFinal()
            .allowEmptyShould(true);

    @ArchTest
    public final ArchRule log2Test = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    public final ArchRule injectionDependencyTest = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
}
