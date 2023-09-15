package com.example.codeclubapp.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.codeclubapp.R

@Composable
fun MyCodeClubImage(){
    //drawable -> pasta onde colocamos os arquivos de imagens
    Image(painter = painterResource(id = R.drawable.logo), contentDescription = "code club logo")
}