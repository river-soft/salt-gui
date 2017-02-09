import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
// import MinionsState from '../components/minions/MinionsState';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Container from 'muicss/lib/react/container';
import * as getMinionsState from '../actions/GetMinionsStateAction';


class Minions extends Component {

    constructor(props) {
        super(props);

        this.state = {
            data: {}
        }
    }

    componentWillReceiveProps(nextProps) {
        console.log(nextProps);
    }

    componentDidMount() {
        const {getMinionsState} = this.props.getMinionsState;

        getMinionsState();
    }

    componentDidUpdate() {
        console.log(this);
    }

    render() {
console.log(this);
        return <div className='wrapper'>
            <Header />
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='3' xs='6' lg='3'>
                            <Row>
                                <Col md='12' xs='12' lg='12'>
                                    <div className='minions__state'>

                                    </div>
                                </Col>
                            </Row>
                            <Row>
                                <Col md='12' xs='12' lg='12'>Пока пусто</Col>
                            </Row>
                        </Col>
                        <Col md='9' xs='6' lg='9'>Пока пусто</Col>
                    </Row>
                </Container>
            </main>
        </div>
    }
}

function mapStateToProps(state) {
    return {
        getMinionsState: state.getMinionsState
    }
}

function mapDispatchToProps(dispatch) {
    return {
        getMinionsState: bindActionCreators(getMinionsState, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Minions)