package com.vkedu.pricewise.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun signIn(email: String, password: String): User? {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user ?: return null
        val userDoc = db.collection("users").document(firebaseUser.uid).get().await()
        return userDoc.toObject(User::class.java)
    }

    suspend fun signUp(email: String, password: String): User {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user!!
        val newUser = User(uid = firebaseUser.uid, email = firebaseUser.email)
        db.collection("users").document(firebaseUser.uid).set(newUser).await()
        return newUser
    }

    suspend fun addFavorite(userId: String, item: FavoriteItem) {
        val userRef = db.collection("users").document(userId)
        db.runTransaction {
            val user = it.get(userRef).toObject(User::class.java)!!
            val updatedFavorites = user.favorites.toMutableList().apply { add(item) }
            it.update(userRef, "favorites", updatedFavorites)
        }.await()
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        // This is a simplified version. For a real app, you'd fetch the full user profile from Firestore.
        return User(uid = firebaseUser.uid, email = firebaseUser.email)
    }

    fun signOut() {
        auth.signOut()
    }
}