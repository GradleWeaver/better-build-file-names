plugins {
    `kotlin-dsl`
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "4.10.2"
    distributionType = Wrapper.DistributionType.ALL
}
