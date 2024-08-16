package app.skypub.user

import androidx.lifecycle.ViewModel
import app.skypub.data.repository.UserRepository

class UserDetailScreenViewModel(
    private val userDetailScreenRepository: UserRepository
) : ViewModel() {
}