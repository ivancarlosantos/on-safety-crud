package ics.on_safety.desafio.crud.archtest;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.GeneralCodingRules;
import ics.on_safety.desafio.crud.utils.ValidateParameter;
import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "ics.on_safety.desafio.crud", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    @ArchTest
    static ArchRule layerTest = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..api..")
            .layer("Services").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Services").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Services");

    @ArchTest
    static ArchRule controllerTest = ArchRuleDefinition.classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..api..")
            .andShould().haveSimpleNameEndingWith("Controller")
            .andShould().haveSimpleNameNotEndingWith("RestController");

    @ArchTest
    static ArchRule servicesTest = ArchRuleDefinition.classes()
            .that().resideInAPackage("..service..")
            .should().beAnnotatedWith(Service.class);

    @ArchTest
    static ArchRule serviceNameHaveBeenFinallyService = ArchRuleDefinition.classes()
            .that().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("Services");

    @ArchTest
    static ArchRule componentIsNotAllowed = ArchRuleDefinition.classes()
            .that().resideInAPackage("..service..")
            .should().notBeAnnotatedWith(Component.class)
            .because("Anotação 'Component' não é permitida no pacote service");

    @ArchTest
    static ArchRule controllerDoNotCallRepositoryTest = ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..api..")
            .should().dependOnClassesThat().resideInAPackage("..repository..")
            .because("Controller não pode chamar diretamente o repository.");

    @ArchTest
    static ArchRule entityTest = ArchRuleDefinition.classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("..model..")
            .because("Classe responsável pela camada de modelo e persistência");

    @ArchTest
    static ArchRule dtoTest = ArchRuleDefinition.classes()
            .that().resideInAPackage("..dto..")
            .should()
            .beRecords()
            .because("Classe responsável pela transferência de dados entre cliente e camada de modelo");

    @ArchTest
    static ArchRule exceptionTest = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(RestControllerAdvice.class)
            .should()
            .resideInAPackage("..exception..")
            .because("Pertence a um pacote de controle de exceções");

    @ArchTest
    static ArchRule exceptionMethodTest = ArchRuleDefinition.methods()
            .that().areAnnotatedWith(ExceptionHandler.class)
            .should().bePublic()
            .because("Classe Responsável por controle e devolutiva de exceção");

    @ArchTest
    static ArchRule repositoryTest = ArchRuleDefinition.classes()
            .that().resideInAPackage("..repository..").should()
            .beAnnotatedWith(Repository.class)
            .because("Repository é uma classe/interface de persistência @Repository");

    @ArchTest
    static ArchRule repositoryClassInterface = ArchRuleDefinition.classes()
            .that().resideInAPackage("..repository..")
            .should().beAnnotatedWith(Repository.class)
            .andShould().beInterfaces().because("Classe de contrato com a data base");

    @ArchTest
    static ArchRule fieldsEntity = ArchRuleDefinition.fields()
            .that().areDeclaredInClassesThat()
            .resideInAPackage("..model..")
            .should().bePrivate()
            .because("atributos devem ser privados e acessados via método/encapsulamento");

    @ArchTest
    static ArchRule controllerMethodsTest = ArchRuleDefinition.noMethods()
            .that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should().beAnnotatedWith(RequestMapping.class)
            .andShould().beAnnotatedWith(RestController.class);

    @ArchTest
    static ArchRule utilsValidateParameterTest = ArchRuleDefinition
            .classes()
            .that().resideInAPackage("..utils..")
            .should()
            .haveSimpleName("ValidateParameter")
            .because("Classe utilitária para conversão de tipo String/Long e seu tratamento em caso de erro");

    @ArchTest
    static ArchRule utilsValidateTest = ArchRuleDefinition
            .classes()
            .that()
            .resideInAPackage("..utils..")
            .should()
            .haveOnlyPrivateConstructors();

    @ArchTest
    static ArchRule utilsValidateMethodTest = ArchRuleDefinition.methods()
            .that().areDeclaredIn(ValidateParameter.class)
            .should().bePublic()
            .because("Para acesso de conversão de tipo");


    @ArchTest
    static ArchRule logTest = ArchRuleDefinition.fields()
            .that().haveRawType(Logger.class)
            .should().bePrivate()
            .andShould().beStatic()
            .andShould().beFinal()
            .allowEmptyShould(true);

    @ArchTest
    static ArchRule log2Test = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    static ArchRule injectionDependencyTest = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
}
