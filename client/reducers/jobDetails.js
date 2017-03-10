import {
    JOB_DETAILS_SUCCESS,
    JOB_DETAILS_FAIL
} from '../constants/JobDetails';

const initialState = {
    jobDetails: '',
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case JOB_DETAILS_SUCCESS:
            return {...state, jobDetails: action.payload, error: ''};
        case JOB_DETAILS_FAIL:
            return {...state, error: action.payload};
        default:
            return state;
    }
}