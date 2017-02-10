import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import MinionsCountsStatus from '../components/minions/MinionsCountsStatus';
import MinionsCountsGroup from '../components/minions/MinionsCountsGroup';
import MinionsAccepted from '../components/minions/MinionsAccepted';
import MinionsUnaccepted from '../components/minions/MinionsUnaccepted';
import MinionsDenied from '../components/minions/MinionsDenied';
import MinionsRejected from '../components/minions/MinionsRejected';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Container from 'muicss/lib/react/container';
import * as minionsAction from '../actions/minionsAction';
import * as getMinionsGroupsAction from '../actions/GetMinionsGroupAction';
import Tabs from 'muicss/lib/react/tabs';
import Tab from 'muicss/lib/react/tab';

class Minions extends Component {

    constructor(props) {
        super(props);

        this.state = {
            client: ''
        }
    }

    componentDidMount() {
        const {minions} = this.props.minionsAction;

        let client = minions();

        this.setState({
            client: client
        })
    }

    componentWillUnmount() {
        this.state.client.disconnect();
    }

    render() {

        const {getMinionsGroups} = this.props.getMinionsGroups;

        let countsStatus = <MinionsCountsStatus countsStatus={this.props.minions.countsStatus}/>,
            countsGroup = <MinionsCountsGroup countsStatus={this.props.minions.countsGroup}/>,
            acceptedMinions = <MinionsAccepted acceptedMinions={this.props.minions.acceptedMinions}/>,
            unacceptedMinions = <MinionsUnaccepted unacceptedMinions={this.props.minions.unacceptedMinions}
            getMinionsGroups={getMinionsGroups}/>,
            deniedMinions = <MinionsDenied deniedMinions={this.props.minions.deniedMinions}/>,
            rejectedMinions = <MinionsRejected rejectedMinions={this.props.minions.rejectedMinions}/>;

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
                        <Col md='9' xs='6' lg='9'><Tabs className='minions-tabs' justified={true}>
                            <Tab className='minions-tabs' label='Accepted'>{acceptedMinions}</Tab>
                            <Tab className='minions-tabs' label='Denied'>{deniedMinions}</Tab>
                            <Tab className='minions-tabs' label='Unaccepted'>{unacceptedMinions}</Tab>
                            <Tab className='minions-tabs' label='Rejected'>{rejectedMinions}</Tab>
                        </Tabs></Col>
                    </Row>
                </Container>
            </main>
        </div>
    }
}

function mapStateToProps(state) {
    return {
        minions: state.minions,
        minionsGroups: state.minionsGroups
    }
}

function mapDispatchToProps(dispatch) {
    return {
        minionsAction: bindActionCreators(minionsAction, dispatch),
        getMinionsGroupsAction: bindActionCreators(getMinionsGroupsAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Minions)