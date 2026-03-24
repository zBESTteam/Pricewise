package com.pricewise.feature.search.impl.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pricewise.core.ui.components.PriceWiseProductCard
import com.pricewise.core.ui.components.PriceWiseProductCardModel
import com.pricewise.feature.search.api.domain.model.Product
import com.valentinilk.shimmer.shimmer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.pricewise.core.ui.R

@Composable
fun ProductCard(product: Product, addToFavourites: (Product) -> Unit) {
    PriceWiseProductCard(
        product = PriceWiseProductCardModel(
            id = product.id,
            title = product.title,
            price = product.price.toRubles(),
            isFavorite = product.isFavorite,
            thumbnailUrl = product.thumbnailUrl,
            marketplaceName = product.merchant.name,
            marketplaceShortName = product.merchant.name.take(2).lowercase(),
            marketplaceLogoUrl = product.merchant.logoUrl,
        ),
        onClick = {},
        onFavoriteClick = { addToFavourites(product) },
        modifier = Modifier,
    )
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
