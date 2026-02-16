package fr.isen.sahar.thegreatestcocktailapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight


import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.blur





@Composable
fun DetailCocktailScreen(modifier: Modifier = Modifier) {

    Box(
        Modifier.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                colorResource( id= R.color.rosebb),
                colorResource( id= R.color.rose)
            )
        ))
        .fillMaxSize() ){
        // fillMaxWidth pour dire prends toute la largeur
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.photo),
                contentDescription = "",
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Yoghurt Cooler",
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )


            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryView("Other/Unknown" , colors = listOf(
                    colorResource(id = R.color.lav),
                    colorResource(id = R.color.jaune)
                ))
                CategoryView("Non alcoholic" , colors = listOf(
                    colorResource(id = R.color.ocean),
                    colorResource(id = R.color.ciel)
                ))
            }


            Text(
                text = "Highball glass",
                fontSize = 18.sp,
                color = Color.White
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.25f),
                        RoundedCornerShape(24.dp)
                    ),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Ingredients",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(text = "* Yoghurt", color = Color.White)
                    Text(text = "* Fruit", color = Color.White)
                }
            }

        }
    }
}


@Composable
fun CategoryView(category: String , colors: List<Color> ){
    Box(Modifier
        .clip(CircleShape)
        .background(
            Brush.horizontalGradient(
                colors = colors.map { it.copy(alpha = 0.25f) } // garde les couleurs mais "verre"
            )
        )
        .border(
            1.dp,
            Color.White.copy(alpha = 0.25f),   // bordure glass
            CircleShape
        )
    ){
        Text(
            text = category,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}


