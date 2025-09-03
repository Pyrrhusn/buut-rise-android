import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservation_details")
data class ReservationDetailsEntity(
    @PrimaryKey
    val reservationId: Int,
    val isDeleted: Boolean,
    val mentorName: String?,
    val batteryId: Int,
    val currentBatteryUserName: String?,
    val currentBatteryUserId: Int,
    val currentHolderPhoneNumber: String?,
    val currentHolderEmail: String?,
    val currentHolderStreet: String?,
    val currentHolderNumber: String?,
    val currentHolderCity: String?,
    val currentHolderPostalCode: String?
)
