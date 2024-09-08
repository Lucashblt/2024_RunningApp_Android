package com.example.runningapp.Data

import com.example.runningapp.R

object UserData {
    var Users : MutableList<User> = mutableListOf<User>()

    init {
        Users.add(User(1, "Lucas", "Hublart", "Nosake","10/01/2003", R.drawable.pp, "1234"))
        Users.add(User(2, "John", "Doe", "john.doe","12/12/2021", R.drawable.likes, "1234"))
        Users.add(User(3, "Jane", "Doe", "jane.doe","12/12/2021", R.drawable.ic_launcher_background, "1234"))
        Users.add(User(4, "Jean", "Dupont", "jean.dupont","12/12/2021", R.drawable.ic_launcher_foreground, "1234"))
        Users.add(User(5, "Jeanne", "Dupont", "jeanne.dupont","12/12/2021", R.drawable.runner, "1234"))
    }

    fun getUserById(id : Int) : User? {
        for (user in Users) {
            if (user.id == id) {
                return user
            }
        }
        return null
    }

    fun verifLogin(username : String, password : String) : Boolean {
        for (user in Users) {
            if (user.username == username && user.mdp == password) {
                return true
            }
        }
        return false
    }

    fun getUserByLoggin(username : String, password : String) : Int {
        for (user in Users) {
            if (user.username == username && user.mdp == password) {
                return user.id
            }
        }
        return -1
    }

    fun getLastId() : Int {
        return Users.last().id
    }

    fun addUser(name : String, surname : String, username : String, date : String, mdp : String) {
        Users.add(User(getLastId() + 1, name, surname, username, date, R.drawable.likes, mdp))
    }

}