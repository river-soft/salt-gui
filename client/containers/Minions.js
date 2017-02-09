import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import MinionsCountsStatus from '../components/minions/MinionsCountsStatus';
import MinionsCountsGroup from '../components/minions/MinionsCountsGroup';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Container from 'muicss/lib/react/container';
import * as minionsAction from '../actions/minionsAction';

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
        const {minions} = this.props.minionsAction;

        minions();
    }

    componentDidUpdate() {
        console.log(this);
    }

    render() {

        let countsStatus = <MinionsCountsStatus countsStatus={this.props.minions.countsStatus}/>,
            countsGroup = <MinionsCountsGroup countsStatus={this.props.minions.countsGroup}/>;

        return <div className='wrapper'>
            <Header />
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='3' xs='6' lg='3'>
                            <Row>
                                <Col md='12' xs='12' lg='12'>
                                    <div className='minions__state'>
                                        {countsStatus}
                                    </div>
                                </Col>
                            </Row>
                            <Row>
                                <Col md='12' xs='12' lg='12'>{countsGroup}</Col>
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
        minions: state.minions
    }
}

function mapDispatchToProps(dispatch) {
    return {
        minionsAction: bindActionCreators(minionsAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Minions)