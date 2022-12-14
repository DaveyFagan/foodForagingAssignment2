package ie.wit.foraging.models

import ie.wit.foraging.models.ForagingModel

interface ForagingStore {
    fun findAll(): List<ForagingModel>
    fun create(foraging: ForagingModel)
    fun update(foraging: ForagingModel)
    fun delete(foraging: ForagingModel)
//    fun createUser(user: UserModel)
//    fun findAllUsers(): List<UserModel>

}