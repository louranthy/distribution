db.createUser(
        {
            user: "diceuser",
            pwd: "diceuser1",
            roles: [
                {
                    role: "readWrite",
                    db: "dicesimulation"
                }
            ]
        }
);