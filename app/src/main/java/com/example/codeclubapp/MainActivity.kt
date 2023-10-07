package com.example.codeclubapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.codeclubapp.ui.theme.CodeClubAppTheme
import com.example.codeclubapp.view.Feed
import com.example.codeclubapp.view.FeedTeacher
import com.example.codeclubapp.view.Home
import com.example.codeclubapp.view.Login
import com.example.codeclubapp.view.LoginFormTeacher
import com.example.codeclubapp.view.ManageFeed
import com.example.codeclubapp.view.ManagePolls
import com.example.codeclubapp.view.ManageProjects
import com.example.codeclubapp.view.ManageStudents
import com.example.codeclubapp.view.ManageTeams
import com.example.codeclubapp.view.Notifications
import com.example.codeclubapp.view.NotificationsTeacher
import com.example.codeclubapp.view.Poll
import com.example.codeclubapp.view.Student
import com.example.codeclubapp.view.Teacher
import com.example.codeclubapp.view.UserAndPassStudent
import com.example.codeclubapp.view.UserAndPassTeacher
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
                        //login user and pass -> teacher
                        route = "login_teacher"
                    ){
                        LoginFormTeacher(navController)
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
                    composable(
                        //feed de noticias -> feed_screen
                        route = "feed_teacher"
                    ){
                        FeedTeacher(navController)
                    }
                    //route = "notifications"
                    composable(
                        //notificacoes -> notification_screen
                        route = "notifications"
                    ){
                        Notifications(navController)
                    }
                    composable(
                        //notificacoes -> notification_screen
                        route = "notifications_teacher"
                    ){
                        NotificationsTeacher(navController)
                    }
                } //NaviHost -> navegacao

            }
        }
    }

    override fun onStart() {
        super.onStart()
        val userIsPresent = FirebaseAuth.getInstance().currentUser
        if(userIsPresent == null) {
            print(" $userIsPresent ")
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    //Firebase.auth.signOut()

    // [START ask_post_notifications]
    /*
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }*/
    // [END ask_post_notifications]
}

