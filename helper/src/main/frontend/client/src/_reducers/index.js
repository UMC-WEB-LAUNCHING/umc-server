import { combineReducers } from "redux";    // store가 있는데 reducer가 여러가지 있음 : 어떻게 state가 변하는지 보여주고, 변한 마지막 값을 리턴해줌(유저, 구독, 댓글 리듀서) -> rootreducer에서 하나로 합쳐줌
import user from './user_reducer';   

const rootReducer = combineReducers({
    user
})

export default rootReducer;