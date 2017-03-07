import {JOB_RESULTS_COUNTS} from '../constants/JobResults';

const initialState = {

    results: '',
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case JOB_RESULTS_COUNTS:
            return {...state, result: action.payload, error: ''};
        default:
            return state;
    }
}