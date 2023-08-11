const headerProvider = require('./headerProvider');
const baseResponse = require("../../../config/baseResponseStatus");
const {response, errResponse} = require("../../../config/response");

/**
 * API No. 1
 * API Name : 헤더에 필요한 정보 조회 API
 * [GET] /header
 */
exports.getHeaderInfo = async (req, res) => {

    try{
        const {userId, teamId} = req.query;

        let HeaderInfo = await headerProvider.retrieveUser(userId);
        if (teamId){
            const teamName = await headerProvider.retrieveTeam(teamId);
            HeaderInfo = {...HeaderInfo, ...teamName};
        }
        return res.send(response(baseResponse.SUCCESS, HeaderInfo));
    } catch(err){
        console.log(err);
        return res.send(errResponse(baseResponse.SERVER_ERROR));
    }
};
