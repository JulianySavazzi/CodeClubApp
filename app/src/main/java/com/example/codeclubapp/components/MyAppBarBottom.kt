package com.example.codeclubapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.codeclubapp.ui.theme.RedCode
import com.example.codeclubapp.ui.theme.WHITE

@Composable
fun MyAppBarBottom(navController: NavController,
                   loginStudent: MutableState<Boolean>,
                   loginTeacher: MutableState<Boolean>
                   ){

    BottomAppBar (
        containerColor = RedCode,
    ){
        val items = listOf("perfil", "feed", "notificações")
        val selectedItem = remember { mutableStateOf(items[0]) }

        NavigationBar (
            containerColor = RedCode,
            contentColor = WHITE
        ){
            items.forEachIndexed { index, item ->
                if(index == 0){
                    //perfil
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Filled.AccountCircle,
                                contentDescription = item[0].toString(),
                                tint = WHITE,
                            ) },
                        //label = { Text(item, color = WHITE) },
                        selected = selectedItem.value == item,
                        onClick = {
                            //se o usuario logado for professor -> abrir Teacher()
                            //se o usuario logado for aluno -> abrir Student()
                            selectedItem.value = item
                            if(loginStudent.value == true) navController.navigate(route = "student")
                            if(loginTeacher.value == true) navController.navigate(route = "teacher")
                            if(loginTeacher.value == false && loginStudent.value == false) navController.navigate(route = "home")
                                  },
                    )
                }
                if(index == 1){

                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Filled.List,
                                contentDescription = item[1].toString(),
                                tint = WHITE
                            ) },
                        //label = { Text(item, color = WHITE) },
                        selected = selectedItem.value == item,
                        onClick = {
                            selectedItem.value = item
                            if(loginStudent.value == true) navController.navigate(route = "feed")
                            if(loginTeacher.value == true) navController.navigate(route = "feed_teacher")

                        }
                    )
                }
                if(index == 2){
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Filled.Notifications,
                                contentDescription = item[2].toString(),
                                tint = WHITE
                            ) },

                        //label = { Text(item, color = WHITE) },
                        selected = selectedItem.value == item,
                        onClick = {
                            //passar variavel com a rota para mudar de tela
                            selectedItem.value = item
                            if(loginStudent.value == true) navController.navigate(route = "notifications")
                            if(loginTeacher.value == true) navController.navigate(route = "notifications_teacher")

                        }
                    )
                }
            }
        }
    }

}
