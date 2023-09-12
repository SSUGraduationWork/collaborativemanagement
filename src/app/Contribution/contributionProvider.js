const {pool} = require("../../../config/database");
const contributionDao = require("./contributionDao");

// Provider: Read 비즈니스 로직 처리

const contributionProvider = {
    retrieveContributions : async (teamId) => {
        const connection = await pool.getConnection(async (conn) => conn);
        const contributionList = await contributionDao.selectContributions(connection, teamId);
        connection.release();
        return contributionList;
    },

    retrieveWorkProgress : async (teamId) => {
        const connection = await pool.getConnection(async(conn) => conn);
        const [workProgress] = await contributionDao.selectWorkProgress(connection, teamId);
        connection.release();
        return workProgress;
    }

}

module.exports = contributionProvider;
