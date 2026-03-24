package com.pricewise.feature.search.impl.presentation.components

import Typography.Inter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import com.pricewise.core.ui.R
import com.pricewise.feature.search.api.domain.model.Product
import com.valentinilk.shimmer.shimmer

@Composable
fun ProductCard(product: Product, addToFavourites: (Product) -> Unit) {

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).components { add(SvgDecoder.Factory()) }.build()
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
            SubcomposeAsyncImage(
                modifier = Modifier
                    .aspectRatio(1.05f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp)),
                model = product.thumbnailUrl,
                contentDescription = null,
                imageLoader = imageLoader,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                            .background(
                                color = colorResource(R.color.light_gray).copy(alpha = 0.6f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            )
            Spacer(modifier = Modifier.size(21.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(18.dp)
                            .height(18.dp),
                        model = product.merchant.logoUrl,
                        contentDescription = null,
                        imageLoader = imageLoader,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmer()
                                    .background(
                                        color = colorResource(R.color.light_gray).copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = product.merchant.name,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            fontFamily = Inter,
                            fontWeight = FontWeight(600),
                            color = colorResource(R.color.black),

                            letterSpacing = 0.3.sp,
                        )
                    )
                }
                Text(
                    text = product.title.take(75),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontFamily = Inter,
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
                        fontFamily = Inter,
                        fontWeight = FontWeight(700),
                        color = colorResource(R.color.black),
                        letterSpacing = 0.3.sp,
                    )
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
        }
        Icon(
            modifier = Modifier
                .padding(vertical = 48.dp)
                .padding(end = 15.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(R.drawable.ic_favorite_disabled),
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
                color = colorResource(R.color.card_background_color),
                shape = RoundedCornerShape(4.dp)
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
                    .background(
                        colorResource(R.color.light_gray).copy(alpha = 0.6f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.size(21.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 55.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .width(18.dp)
                            .height(18.dp)
                            .background(
                                colorResource(R.color.light_gray).copy(alpha = 0.6f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .background(
                                colorResource(R.color.light_gray).copy(alpha = 0.6f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }

                Column {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .background(
                                    colorResource(R.color.light_gray).copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .background(
                            colorResource(R.color.light_gray).copy(alpha = 0.6f),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 48.dp)
                    .padding(end = 15.dp)
                    .background(
                        colorResource(R.color.light_gray).copy(alpha = 0.6f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 15.dp)
                .size(30.dp)
                .background(
                    colorResource(R.color.light_gray).copy(alpha = 0.6f),
                    shape = RoundedCornerShape(100.dp)
                )
        )
    }
}