import {
     GET_MESSAGES_SUCCESS,
    GET_MESSAGES_FAIL
} from '../constants/GetMessages';

const initialState = {
    messages: [],
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case GET_MESSAGES_SUCCESS:
            return {...state, messages: action.payload, error: ''};
        case GET_MESSAGES_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}