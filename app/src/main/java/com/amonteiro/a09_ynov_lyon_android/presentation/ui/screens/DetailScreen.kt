package com.amonteiro.a09_ynov_lyon_android.presentation.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.amonteiro.a09_ynov_lyon_android.R
import com.amonteiro.a09_ynov_lyon_android.domain.model.Weather
import com.amonteiro.a09_ynov_lyon_android.presentation.ui.theme.AppTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
@Composable
fun DetailScreenPreview() {
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DetailScreen(
                modifier = Modifier.padding(innerPadding),
                //jeu de données pour la Preview
                data = Weather(
                    id = 1,
                    name = "Nice",
                    temp = 18.5,
                    description = "ciel dégagé",
                    icon = "https://picsum.photos/200",
                    speed = 5.0
                )
            )
        }
    }
}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    data: Weather,
    onBackClick: () -> Unit = {}
) {

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = data.name, fontSize = 44.sp,
            color = MaterialTheme.colorScheme.primary
            )



        //Permission Internet nécessaire
        AsyncImage(
            model = data.icon,
            //Pour aller le chercher dans strings.xml R de votre package com.nom.projet
            //contentDescription = stringResource(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            contentScale = ContentScale.FillWidth,

            //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
            //Image d'échec de chargement qui sera utilisé par la preview
            error = painterResource(R.drawable.error),
            //Image d'attente.
            //placeholder = painterResource(R.drawable.toto),

            onError = { println(it) },
            modifier = Modifier.fillMaxWidth().weight(2f)
        )


        Text(
            text = data.getResume(), fontSize = 20.sp,
            modifier = Modifier.padding(10.dp).fillMaxWidth().weight(1f)
        )

        Button(
            onClick = onBackClick,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Retour")
        }
    }
}