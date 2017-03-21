import {
    JOB_RESULTS_COUNTS,
    JOB_RESULTS_SCRIPT
} from '../constants/JobResults';

const initialState = {

    results: '',
    jobScriptResults: '',
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case JOB_RESULTS_COUNTS:
            return {...state, result: action.payload, error: ''};
        case JOB_RESULTS_SCRIPT:
            return {...state, jobScriptResults: action.payload, error: ''};
        default:
            return state;
    }
}