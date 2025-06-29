## Solutions Test technique SkyPay
#### Skypay Technical Test 1 - Banking Service:
![](/assets/bs-run.png)
![](/assets/bs-test.png)
#### Skypay Technical Test 2 - Hotel Reservation System:
![](/assets/hrs-run.png)
1. No putting all functions on the same Service doesn't respect the separation of concerns and makes the code harder to test and maintain. It's better to split logic into `UserService`, `RoomService`, and `BookingService`
2. `setRoom` should only create new rooms and not allow the modification of existing ones