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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.example.pricewise.R
import com.example.pricewise.feature.main.domain.model.Product
import com.valentinilk.shimmer.shimmer

@Composable
fun ProductCard(product: Product, addToFavourites: (Product) -> Unit) {
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
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp)),
                model = product.thumbnailUrl,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(21.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(18.dp)
                            .height(18.dp),
                        model = product.merchant.logoUrl,
                        imageLoader = ImageLoader.Builder(LocalContext.current)
                            .components {
                                add(SvgDecoder.Factory())
                            }
                            .build(),
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
                    text = product.title.take(80),
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
            Spacer(modifier = Modifier.size(30.dp))
        }
        Icon(
            modifier = Modifier
                .padding(vertical = 48.dp)
                .padding(end = 15.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(R.drawable.ic_favourite_disabled),
            contentDescription = null
        )
    }
}

private fun Long.toRubles(): String {
    return "%,d ₽".format(this).replace(',', ' ')
}

@Composable
fun ProductCardShimmer() {
    Box(
        modifier = Modifier
            .height(113.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.card_background_color)
            )
            .shimmer(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp)
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1.05f)
                    .fillMaxSize()
                    .background(colorResource(R.color.light_gray).copy(alpha = 0.6f))
            )

            Spacer(modifier = Modifier.size(21.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(18.dp)
                            .height(18.dp)
                            .background(colorResource(R.color.light_gray).copy(alpha = 0.6f))
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .background(colorResource(R.color.light_gray).copy(alpha = 0.6f))
                    )
                }

                Column {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .background(colorResource(R.color.light_gray).copy(alpha = 0.6f))
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .background(colorResource(R.color.light_gray).copy(alpha = 0.6f))
                )
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 48.dp)
                    .padding(end = 15.dp)
                    .background(colorResource(R.color.light_gray).copy(alpha = 0.6f))
            )
        }
    }
}