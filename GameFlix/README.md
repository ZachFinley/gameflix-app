The backend can be accessed through a Docker container after building with the command:

docker compose up --build -d

This must be run in the root directory which contains the Springboot app.
Running just the Docker image which contains the APIs will cause an error since it cannot read the database.
A second image is added that makes a connection to mySQL and port 3307 in the event port 3306 is in use. The sql file needs to be run on port 3307 as well.
The command listed above should run both images at the same time.
Once this command is run, the Docker container can be run or stopped in the Docker desktop app.