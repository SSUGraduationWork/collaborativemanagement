const workProvider = require('./workProvider');
const workService = require('./workService');
const baseResponse = require("../../../config/baseResponseStatus");
const {response, errResponse} = require("../../../config/response");

/**
 * API No. 1
 * API Name : 전체 작업 조회 API
 * [GET] /work/:teamId
 */
exports.getWorks = async (req, res) => {

    try{
        const teamId = req.params.teamId;

        const workList = await workProvider.retrieveWorkList(teamId);
        return res.send(response(baseResponse.SUCCESS, workList));
    } catch(err){
        console.log(err);
        return res.send(errResponse(baseResponse.SERVER_ERROR));
    }
};

/**
 * API No. 2
 * API Name : 작업 등록 API
 * [POST] /work/:teamId
 */
exports.postWork = async (req, res) => {

    /**
     * Body: workName, worker,endDate, importance, status
     */
    const {workName, worker, endDate, importance, status} = req.body;
    const teamId = req.params.teamId;
    let workerNumber;
    let worker_arr = [];

    if (worker) {
        worker_arr = JSON.parse(worker);
    }

    workerNumber = worker_arr.length;

    const insertWorkParams = [
        teamId, workName, endDate, importance, status, workerNumber
    ];

    const postWorkResult = await workService.createWork(insertWorkParams, worker_arr);

    return res.send(postWorkResult);

};

/**
 * API No. 3
 * API Name : 특정 작업 조회 API
 * [GET] /work/:teamId/:workId
 */
exports.getWorkById = async (req, res) => {
    try {
        const workId = req.params.workId;
        const workById = await workProvider.retrieveWork(workId);
        return res.send(response(baseResponse.SUCCESS, workById));
    } catch(err){
        console.log(err);
        return res.send(errResponse(baseResponse.SERVER_ERROR));
    }
};

/**
 * API No. 4
 * API Name : 특정 작업 삭제 API
 * [DELETE] /work/:teamId/:workId
 */
exports.deleteWork = async function (req, res) {

    const workId = req.params.workId;

    const deleteWorkResult = await workService.deleteWorkById(workId);

    return res.send(deleteWorkResult);
};


/**
 * API No. 5
 * API Name : 특정 작업 수정 API
 * [PUT] /work/:teamId/:workId
 * body : workName, worker,endDate, importance, status
 */

exports.putWork = async function (req, res) {


    const workId = req.params.workId;

    const {workName, worker, endDate, importance, status} = req.body;

    let worker_arr = JSON.parse(worker);
    const workerNumber = worker_arr.length;

    const updateWorkParams = [
        workName, endDate, importance, status, workerNumber
    ];
    const editWorkResult = await workService.editWork(workId, updateWorkParams, worker_arr);
    res.send(editWorkResult);

};
