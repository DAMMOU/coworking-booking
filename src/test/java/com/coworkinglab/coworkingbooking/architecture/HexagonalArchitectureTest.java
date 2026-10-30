package com.coworkinglab.coworkingbooking.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.coworkinglab.coworkingbooking",
        importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule domain_should_not_depend_on_frameworks =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "org.springframework..",
                            "jakarta.persistence..",
                            "com.fasterxml.jackson..");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_other_layers =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..application..",
                            "..infrastructure..");

    @ArchTest
    static final ArchRule application_should_not_depend_on_infrastructure =
            noClasses()
                    .that().resideInAPackage("..application..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..infrastructure..");

    @ArchTest
    static final ArchRule application_should_not_depend_on_spring =
            noClasses()
                    .that().resideInAPackage("..application..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("org.springframework..");

    @ArchTest
    static final ArchRule infrastructure_adapters_should_implement_ports =
            classes()
                    .that().resideInAPackage("..adapter.out.persistence.adapter..")
                    .should().implement(
                            com.tngtech.archunit.base.DescribedPredicate.describe(
                                    "a port outbound",
                                    javaClass -> javaClass.getPackageName()
                                            .startsWith("com.coworkinglab.coworkingbooking.application.port.out")));

    @ArchTest
    static final ArchRule use_cases_should_be_named_use_case =
            classes()
                    .that().resideInAPackage("..application.port.in..")
                    .should().haveSimpleNameEndingWith("UseCase");

    @ArchTest
    static final ArchRule services_should_be_named_service =
            classes()
                    .that().resideInAPackage("..application.service..")
                    .should().haveSimpleNameEndingWith("Service");
}