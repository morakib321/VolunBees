# VolunBee

VolunBee is a volunteering and community engagement platform where volunteers can offer a helping hand to those in need, test and share their knowledge, and connect with fellow volunteers. The app serves two types of users: volunteers and help seekers. Help seekers can request assistance by specifying their needs and the type of volunteers required.

The name **VolunBee** combines the words "Volunteer" and "Bee". Bees symbolize teamwork and generosity; they work together harmoniously and help each other accomplish their tasks. This reflects the core values of the platform: collaboration, support, and community spirit.

---

### Problem Statement 

Many communities struggle to connect volunteers with people in need efficiently. VolunBee addresses this by providing a centralized platform where:

	•	Volunteers can find opportunities to help and share their knowledge.
 
	•	Help seekers can request assistance for events, tasks, or emergency needs.
 
	•	Communities can promote events, track volunteer engagement, and encourage participation through achievements and leaderboards.

The app’s goal is to encourage community engagement while making volunteering more accessible and rewarding.
### Tech Stack
	•	Platform: Android
	•	IDE: Android Studio
	•	Language: Java 
	•	Database: SQLite (local storage)
	•	UI Components: XML layouts, Spinners, Buttons, Notifications

### Features
	•	User authentication with login and registration
	•	Password strength notifications
	•	Age validation (18+ users only)
	•	Volunteer and Help Seeker roles with different home pages
	•	Event posting and participation
	•	Achievement tracking and leaderboard
	•	Quizzes for Healthcare, Intelligence, and Safety Awareness
	•	Community page for messages and image posts
	•	Profile page showing user stats, hours, and quiz scores

## Application Output

### Login Page

<p align="center">
  <img src="https://github.com/user-attachments/assets/f3d980cc-0057-4c32-84f0-2b2d8ab4a033" width="121" height="269" alt="Login Page" />
</p>

The login page consists of two input fields, the username and the password. If the user does not have an account in VolunBee application, they will have to click on the **“New User? Register”** button to create a new account.

---

### Registration Page

<p align="center">
  <img src="https://github.com/user-attachments/assets/3f2d5e6c-281d-4718-a5f1-31b122e32db1" width="145" height="320" alt="Registration Page" />
</p>

The user will input their name, age, gender, phone number, username, password, nationality, and their role.  

- Age is restricted to 18 and above.  
- The username is unique across users.  
- The password must be strong; a notification will show if the password is weak or strong.  
- Nationality is selected using a dropdown (`Spinner`) class.

---

### Password Notifications

<p align="center">
  <img src="https://github.com/user-attachments/assets/9e51f7b9-183f-4550-8c81-7fc25beb1618" width="312" height="162" alt="Weak Password Notification" />
</p>

The figure above shows a notification if the password is not strong. The password requires **more than 8 characters with upper and lower cases, numbers, and special characters**.

<p align="center">
  <img src="https://github.com/user-attachments/assets/cc802cda-eeb2-497b-b7ef-986cebfda49c" width="312" height="147" alt="Password Mismatch" />
</p>

The figure above shows if the password is strong but re-entering the password does not match the first input.

<p align="center">
  <img src="https://github.com/user-attachments/assets/fc1b2218-8ab1-4700-936f-1b8e307a07a2" width="324" height="153" alt="Password Match Notification" />
</p>

The figure above shows a notification when the passwords match and are strong.

---

### Age Restriction Notification

<p align="center">
  <img src="https://github.com/user-attachments/assets/4501f355-54e5-4406-b3eb-d74226195e67" width="163" height="358" alt="Underage Notification" />
</p>

If the user is under 18 years old and clicks **“Register”**, a notification will appear saying that only 18 and above users are eligible.

Once the user creates an account, they will be taken to the login page and log in with their account.

---

## Home Page

### Home Page for a Volunteer

<p align="center">
  <img src="https://github.com/user-attachments/assets/d5828e5c-eab8-4024-a3c9-40b0cd4c8029" width="156" height="341" alt="Volunteer Home Page" />
</p>

The home page displays:  
- Posts of events for volunteering  
- User’s achievements (locked by default until earned)  
- Leaderboard based on achievement scores  

---

### Home Page for a Help Seeker

<p align="center">
  <!-- Replace with your Help Seeker Home Page screenshot -->
  <img width="164" height="364" alt="image" src="https://github.com/user-attachments/assets/2de096cd-31ce-45c3-b3c4-dd38c515783a" />


The Help Seeker home page shows:  
- Events they requested help for  
- Status of requests  
- Any responses from volunteers  

---

### Event Page

<p align="center">
  <!-- Replace with your Event Page screenshot -->
<img width="152" height="337" alt="image" src="https://github.com/user-attachments/assets/6cc63199-7899-4857-80ae-6cd0ed20ced5" />


The Event Page displays:  
- Event details and description  
- Volunteer sign-up options  
- Event date, time, and location  

---

### Achievements Page

<p align="center">
  <!-- Replace with your Achievements Page screenshot -->
<img width="152" height="337" alt="image" src="https://github.com/user-attachments/assets/c9c70482-7794-4294-b7a3-abfcc3152d3b" />


The Achievements Page shows:  
- Completed achievements  
- Points earned for each achievement  
- Leaderboard ranking  

---

### Notifications Page

<p align="center">
  <!-- Replace with your Notifications Page screenshot -->
<img width="162" height="359" alt="image" src="https://github.com/user-attachments/assets/f771876d-51ae-4403-bc04-d1bc66e7b21e" />


The Notifications Page displays:  
- Alerts for new events, responses, or achievements  
- Important system messages  

---
## Quizzes Page

The Quizzes Page features different genres of quizzes: **Healthcare/First Aid**, **Intelligence**, and **Safety & Awareness**.  

---

### Healthcare / First Aid Quiz

<div style="display: flex; gap: 20px; align-items: flex-start;">
  <div>
    <p align="center"><strong>Quiz Page</strong></p>
    <p align="center">
<img width="145" height="319" alt="image" src="https://github.com/user-attachments/assets/ef1ad31f-9185-485b-b9bc-3893a985afa7" />
    </p>
    <p>The first quiz is about healthcare/first aid.</p>
    <p align="center">
    <img width="134" height="291" alt="image" src="https://github.com/user-attachments/assets/320a1e2f-1d82-4b7c-b7d9-7d4cf69c0a99" />
    </p>
  </div>
  <p align="center">
  <img width="350" height="159" alt="image" src="https://github.com/user-attachments/assets/ed96d636-be34-44f7-b3eb-24806c00a8a0" />
  </p>

  <div>
    <p align="center"><strong>Wrong Answer Notification</strong></p>
    <p align="center">
<img width="333" height="151" alt="image" src="https://github.com/user-attachments/assets/7c0e915c-28ec-4019-a27f-4e0520ffd4f7" />
    </p>
    <p>If the user selects a wrong answer, a red notification appears and the submit button is disabled.</p>
  </div>
</div>

<p align="center">
<img width="345" height="156" alt="image" src="https://github.com/user-attachments/assets/5e91260d-f58d-4387-9dbe-9e040ae748f5" />
</p>

<p align="center">
<img width="347" height="157" alt="image" src="https://github.com/user-attachments/assets/d4318b56-a1ee-4c6b-a10c-75699a2e57ad" />
</p>

<p align="center">
<img width="357" height="164" alt="image" src="https://github.com/user-attachments/assets/fe903473-f9d1-47e8-974c-3bf127b572f0" />
</p>

- The user taps the correct position for CPR; green notification for correct, red for incorrect.  
- Chest compressions are counted automatically up to 30.

---

### Intelligence Quizzes

<div style="display: flex; gap: 20px; align-items: flex-start;">
  <p align="center">
  <img width="148" height="329" alt="image" src="https://github.com/user-attachments/assets/d362e2c0-2119-43d5-9f9a-3dfbfd25aca8" />
  </p>

  <div>
    <p align="center"><strong>Intelligence Quiz 1</strong></p>
    <p align="center">
    <img width="152" height="338" alt="image" src="https://github.com/user-attachments/assets/4000cec7-2c33-4d33-aa7c-b56966dad311" />
    </p>


  </div>
  <div>
    <p align="center"><strong>Intelligence Quiz 2</strong></p>
    <p align="center">
 <img width="139" height="306" alt="image" src="https://github.com/user-attachments/assets/0218ec7a-42db-47af-9fb5-7e902e598df0" />
    </p>

  </div>
</div>

- Each quiz has **6 questions** with a **5-second timer per question**.  
- Rewards for correct completion:  
  - **Teacher Level Genius** for the first quiz  
  - **Wizard** for the second quiz  

---

### Safety / Awareness Quiz

<p align="center">
  <img width="139" height="306" alt="image" src="https://github.com/user-attachments/assets/01215e91-52fd-4a18-8ce1-3db8e0066b1b" />

- Follows the same structure as the Intelligence quizzes.  
- Timed questions with rewards for correct completion.

---

## Community Page

<p align="center">
<img width="148" height="327" alt="image" src="https://github.com/user-attachments/assets/b796fdf3-eef2-44e1-9152-f88ec05e6e3a" />
</p>

- Users can write messages and add pictures.  
- All posts are visible to the community, similar to social media feeds.

---

## Profile Page

<p align="center">
<img width="151" height="335" alt="image" src="https://github.com/user-attachments/assets/cc6eee6c-8a8a-4758-8c7d-e71aacc9fd0a" />
</p>

- Displays user’s **name**, **volunteering hours**, and **quiz scores**.  
- Provides a summary of achievements and progress within the app.

## How to Use the Project
	1.	Clone or download the repository
  2.	Open in Android Studio:
     
	    •	Launch Android Studio → File > Open → select the VolunBee project folder.
  4.	Build the project:
     
    	•	Wait for Gradle to finish syncing and building.

	4.	Run the app:

	    •	Connect an Android device or start an emulator.
	    •	Click the Run (green arrow) button in Android Studio.

	5.	Explore the app:

    	•	Log in or register as a volunteer or help seeker.
	    •	Check out events, quizzes, achievements, and the community page.
