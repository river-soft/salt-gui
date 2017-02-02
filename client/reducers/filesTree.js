import {
    FILES_SUCCESS,
    FILES_FAIL
} from '../constants/FilesTree';

const initialState = {
    files: '',
    error: ''
};

export default function filesTree(state = initialState, action) {

    switch (action.type) {
        case FILES_SUCCESS:
            return {...state, files: action.payload, error: ''};

        case FILES_FAIL:
            return {...state, error: action.payload.message};

        default:
            return state
    }
}