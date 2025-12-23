package com.example.pricewise.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pricewise.R
import com.example.pricewise.feature.main.domain.model.Merchant
import com.example.pricewise.feature.main.domain.model.ProductRecommendation
import com.example.pricewise.feature.search.domain.model.Product

@Composable
fun ProductCard(product: ProductRecommendation, addToFavourites: (Product) -> Unit) {
    val inter = FontFamily(
        Font(R.font.inter_regular, weight = FontWeight.W400),
        Font(R.font.inter_medium, weight = FontWeight.W500),
        Font(R.font.inter_semibold, weight = FontWeight.W600),
        Font(R.font.inter_bold, weight = FontWeight.W700),
    )
    Box(
        modifier = Modifier
            .height(113.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.card_background_color),
                shape = RoundedCornerShape(14.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1.05f)
                    .fillMaxSize(),
                model = product.thumbnailUrl,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(21.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(18.dp)
                            .height(18.dp),
                        model = product.merchant.logoUrl,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = product.merchant.name,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            fontFamily = inter,
                            fontWeight = FontWeight(600),
                            color = colorResource(R.color.black),

                            letterSpacing = 0.3.sp,
                        )
                    )
                }
                Text(
                    text = product.title,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontFamily = inter,
                        fontWeight = FontWeight(400),
                        color = colorResource(R.color.black),

                        letterSpacing = 0.3.sp,
                    )
                )
                Text(
                    text = product.price.toRubles(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        fontFamily = inter,
                        fontWeight = FontWeight(700),
                        color = colorResource(R.color.black),
                        letterSpacing = 0.3.sp,
                    )
                )
            }
        }
    }
}

private fun Long.toRubles(): String {
    var result = ""
    var price = this
    while (price > 0) {
        result = " " + price % 1000 + result
    }
    result += " ₽"
    return result
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    ProductCard(
        product = ProductRecommendation(
            id = "TOvar",
            "Iphone XS PRo max ultra super duper",
            100000,
            Merchant(
                "id",
                "ozonzonzon",
                "https://cdn-1.webcatalog.io/catalog/ozon/ozon-icon-filled-256.png?v=1714780866200"
            ),
            "https://apple-com.ru/image/cache/catalog/product/iPhone%2011/iphone_11_b_2-800x540h.jpg.webp",
            false
        ), addToFavourites = {}
    )
}