const contributionProvider = require("./contributionProvider");
const baseResponse = require("../../../config/baseResponseStatus");
const {response, errResponse} = require("../../../config/response");


/**
 * API No. 1
 * API Name : 전체 기여도 조회 API
 * [GET] /contribution/:teamId
 */
exports.getContributions = async (req, res) => {

    try{
        const teamId = req.params.teamId;

        const contributionList = await contributionProvider.retrieveContributions(teamId);
        const workProgress = await contributionProvider.retrieveWorkProgress(teamId);
        const result = {workProgress, contribution : contributionList}
        return res.send(response(baseResponse.SUCCESS, result));
    } catch(err){
        console.log(err);
        return res.send(errResponse(baseResponse.SERVER_ERROR));
    }
};
