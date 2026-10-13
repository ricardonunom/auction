package com.auctionhouse.infrastructure;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Maven modules already stop domain/application from importing infrastructure.
 * These rules cover what Maven can't: frameworks sneaking into the core, and adapters coupling to each other.
 */
@AnalyzeClasses(packages = "com.auctionhouse", importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule domain_is_framework_free = noClasses()
            .that().resideInAPackage("com.auctionhouse.domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..", "jakarta.persistence..", "org.hibernate..", "com.fasterxml..", "tools.jackson..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule application_is_framework_free = noClasses()
            .that().resideInAPackage("com.auctionhouse.application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..", "jakarta.persistence..", "org.hibernate..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule inbound_adapters_do_not_use_outbound_adapters = noClasses()
            .that().resideInAPackage("..adapter.in..")
            .should().dependOnClassesThat().resideInAPackage("..adapter.out..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule outbound_adapters_do_not_use_inbound_adapters = noClasses()
            .that().resideInAPackage("..adapter.out..")
            .should().dependOnClassesThat().resideInAPackage("..adapter.in..")
            .allowEmptyShould(true);
}
