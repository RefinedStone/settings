plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.jpa") version "1.8.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // just add these dependencies for use kotlin-jdsl
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    val jdslVersion = "2.2.1.RELEASE"
//    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter-jakarta:$jdslVersion")

    // coroutine
    val coroutineVersion = "1.6.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutineVersion")

    // reactive
//    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-hibernate-reactive-jakarta:$jdslVersion")
//    implementation("org.hibernate.reactive:hibernate-reactive-core:2.0.8.Final")
//    implementation("org.hibernate:hibernate-core:6.2.0.Final")
    implementation("io.smallrye.reactive:mutiny-kotlin:2.3.0")

    // h2 db reactive
//    implementation("io.agroal:agroal-pool:2.0")
//    implementation("com.h2database:h2")
//    implementation("io.vertx:vertx-jdbc-client:4.5.0")

    /*신규 추가*/
    // Logging
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.5.3")

    // JPA
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.4")

    // Hibernate Reactive
    implementation("org.hibernate.reactive:hibernate-reactive-core:2.3.0.Final")
    implementation("io.vertx:vertx-jdbc-client:4.5.7")
    implementation("io.agroal:agroal-pool:2.3")

    // H2 Database
    runtimeOnly("com.h2database:h2:2.2.224")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")

    // jdsl
    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.4.1")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.4.1")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.4.1")
    implementation("com.linecorp.kotlin-jdsl:hibernate-reactive-support:3.4.1")
    //
    noArg {
        annotation("com.linecorp.kotlinjdsl.example.hibernate.reactive.jakarta.jpql.entity.annotation.CompositeId")
    }

    allOpen {
        annotation("com.linecorp.kotlinjdsl.example.hibernate.reactive.jakarta.jpql.entity.annotation.CompositeId")
        annotation("jakarta.persistence.Entity")
        annotation("jakarta.persistence.Embeddable")
    }
    // jakarta inject
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    // jakarta
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:3.0.0")
}

kotlin {
    jvmToolchain(17)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
