const {pool} = require("../../../config/database");
const workDao = require("./workDao");

// Provider: Read 비즈니스 로직 처리

const workProvider = {
    retrieveWorkList : async (teamId) => {
        const connection = await pool.getConnection(async (conn) => conn);
        const workResult = await workDao.selectWorks(connection, teamId);
        const teamMembers = await workDao.selectTeamMembers(connection, teamId);
        const result = {teamMembers : teamMembers, works : workResult,};
        connection.release();
        return result;
    },

    retrieveWork : async (workId) => {
        const connection = await pool.getConnection(async (conn) => conn);
        const workResult = await workDao.selectWorkById(connection, workId);
        const boardsResult = await workDao.selectBoardsAboutWork(connection, workId);
        const result = {...workResult[0], ...boardsResult}
        connection.release();
        return result;
    }

}

module.exports = workProvider;
