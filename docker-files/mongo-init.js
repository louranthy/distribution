db.auth('admin', 'admin1');
db = db.getSiblingDB('admin');
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
ds = db.getSiblingDB('dicesimulation');
ds.createCollection("dicesimcollection");