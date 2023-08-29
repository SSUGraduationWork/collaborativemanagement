const {pool} = require("../../../config/database");
const headerDao = require("./headerDao");

// Provider: Read 비즈니스 로직 처리

const headerProvider = {
    retrieveUser : async (userId) => {
        const connection = await pool.getConnection(async (conn) => conn);
        const [userInfo] = await headerDao.selectUser(connection, userId);
        connection.release();
        return userInfo;
    },

    retrieveTeam : async (teamId) => {
        const connection = await pool.getConnection(async (conn) => conn);
        const [teamName] = await headerDao.selectTeamName(connection, teamId);
        connection.release();
        return teamName;
    }

}

module.exports = headerProvider;
