package com.example.codeclubapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.codeclubapp.ui.theme.CodeClubAppTheme
import com.example.codeclubapp.view.Feed
import com.example.codeclubapp.view.Home
import com.example.codeclubapp.view.Login
import com.example.codeclubapp.view.ManageFeed
import com.example.codeclubapp.view.ManagePolls
import com.example.codeclubapp.view.ManageProjects
import com.example.codeclubapp.view.ManageStudents
import com.example.codeclubapp.view.ManageTeams
import com.example.codeclubapp.view.Notifications
import com.example.codeclubapp.view.Poll
import com.example.codeclubapp.view.Student
import com.example.codeclubapp.view.Teacher
import com.example.codeclubapp.view.UserAndPassStudent
import com.example.codeclubapp.view.UserAndPassTeacher

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeClubAppTheme {
                //mani é a primeira tela renderizada pelo app
                //configurar rotas do app -> navegacao entre telas
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home"){
                    //tela inicial -> home
                    composable(
                        //configurar as telas pelo uiid
                        //cada composable configura uma rota com seu uiid
                        route = "home"
                    ){
                        //passando a tela home pelo metodo composable
                        Home(navController)
                    }
                    composable(
                        //tela de login -> login
                        route = "login"
                    ){
                        Login(navController)
                    }
                    composable(
                        //tela de login -> login
                        route = "user_student"
                    ){
                        UserAndPassStudent(navController)
                    }
                    composable(
                        //tela de login -> login
                        route = "user_teacher"
                    ){
                        UserAndPassTeacher(navController)
                    }
                    composable(
                        //perfil professor -> teacher_screen
                        route = "teacher"
                    ){
                        Teacher(navController)
                    }
                    composable(
                        //cadastro aluno -> crud_student
                        route = "manageStudents"
                    ){
                        ManageStudents(navController)
                    }
                    composable(
                        //cadastro projeto -> crud_project
                        route = "manageProjects"
                    ){
                        ManageProjects(navController)
                    }
                    composable(
                        //cadastro equipe -> crud_team
                        route = "manageTeams"
                    ){
                        ManageTeams(navController)
                    }
                    composable(
                        //cadastro votacao -> crud_poll
                        route = "managePolls"
                    ){
                        ManagePolls(navController)
                    }
                    composable(
                        //cadastro noticia -> crud_feed
                        route = "manageFeed"
                    ){
                        ManageFeed(navController)
                    }
                    composable(
                        //perfil aluno -> student_screen
                        route = "student"
                    ){
                        Student(navController)
                    }
                    composable(
                        //votacao -> poll_screen
                        route = "poll"
                    ){
                        Poll(navController)
                    }
                    composable(
                        //feed de noticias -> feed_screen
                        route = "feed"
                    ){
                        Feed(navController)
                    }
                    //route = "notifications"
                    composable(
                        //notificacoes -> notification_screen
                        route = "notifications"
                    ){
                        Notifications(navController)
                    }
                } //NaviHost -> navegacao

            }
        }
    }
}

