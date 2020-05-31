# Distribution
Dice Relative Distribution Project

# Application 
  - Run the `start.sh` script to run the application. Note that you should have docker installed in your machine.

  - Once running, you may access the application swagger ui in **http://localhost/dice-api/swagger-ui.html** 

# MongoDB
  - Access the mongodb using a client with the exposed port 27107.

  

     You can change the configurations (e.g. port number, mongodb admin user, db, etc) by changing the values in the env.local file inside the docker-files folder. 
     You may also change the docker-compose.yaml for your preferred configurations.
     
# Containers
  - `start.sh` will run the docker images (diceapi, dicenginx and dicemongo). Containers are using the dice network.
  -  If you have closed the `start.sh`, you may re-execute it to still find your data. volumes aren't remove automatically. `removeContainers.sh` will stop and remove all containers including volumes. Executing `start.sh`
after `removeContainers.sh` will start a fresh database.
  - Containers are using the dice network. The nginx will be using diceapi as the hostname of the api application and
the web will be host.docker.internal as it is outside the dice network. The web is not containerized at the moment.

     