plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pricewise.feature.product.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
