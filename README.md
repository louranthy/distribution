# Distribution
Dice Relative Distribution Project - Web

# Application 
  - Run the `npm install` to add all dependencies of the project.
  - Please follow the instructions in the dice-api branch first. 
  - Run `npm start`. The port used is 4200 and is being proxied by the nginx docker container run from the dice-api branch.
      If you want to change the port, please also change the port being proxied by the nginx in the nginx.conf located
    in the dice-api branch (docker-files/nginx.conf). Restart your dice-api applications.
  - Open **http://localhost/** in your browser. 
