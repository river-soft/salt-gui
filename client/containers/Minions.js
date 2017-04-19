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
import * as acceptMinionsAction from '../actions/AcceptMinionsAction';
import * as rejectMinionsAction from '../actions/RejectMinionsAction';
import * as deleteMinionsAction from '../actions/DeleteMinionsAction';
import * as getMessagesAction from '../actions/GetMessagesAction';
import Tabs from 'muicss/lib/react/tabs';
import Tab from 'muicss/lib/react/tab';
import {containsRole, changeLanguage} from '../helpers';
import cookie from 'react-cookie';

class Minions extends Component {

    constructor(props) {
        super(props);

        this.state = {
            client: '',
            acceptedMinions: false,
            strings: ''
        }
    }

    componentWillMount() {
        if (!this.props.localization) {
            const {getMessages} = this.props.getMessagesAction;

            getMessages();
        } else {
            this.setState({strings: this.props.localization.messages});
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

    setLanguage(locale) {
        changeLanguage(locale);
        this.state.strings.setLanguage(locale);
        this.setState({changeLocale: true});
    }

    setRejectedFalse() {
        this.props.rejectMinions.rejected = false;
    }

    setDeletedFalse() {
        this.props.deleteMinions.deleted = false;
    }

    setDeleteErrorFalse() {
        this.props.deleteMinions.error = '';
        this.setState({update: true});
    }

    render() {

        let token = cookie.load('accessToken'),
            user = token ? JSON.parse(atob(token)) : null;

        if (user && typeof user.roles === 'string') {
            user.roles = user.roles.replace(/[\[\]]/g, '').split(',');
        }

        const {getMinionsGroups} = this.props.getMinionsGroupsAction,
            {acceptMinions} = this.props.acceptMinionsAction,
            {rejectMinions} = this.props.rejectMinionsAction,
            {deleteMinions} = this.props.deleteMinionsAction,
            acceptMinionsSuccess = this.props.acceptMinions.minions,
            rejectMinionsSuccess = this.props.rejectMinions.rejected,
            deleteMinionsSuccess = this.props.deleteMinions.deleted;

        let deleteMinionsError = this.props.deleteMinions.error,
            messages = this.state.strings;

        let countsStatus = containsRole(user.roles, ['ROLE_SHOW_MINIONS_COUNTS_STATUS', 'ROLE_ROOT']) ?
                <MinionsCountsStatus countsStatus={this.props.minions.countsStatus} messages={messages}/> : null,

            countsGroup = containsRole(user.roles, ['ROLE_SHOW_MINIONS_COUNTS_GROUP', 'ROLE_ROOT']) ?
                <MinionsCountsGroup countsStatus={this.props.minions.countsGroup} messages={messages}/> : null,

            acceptedMinions = containsRole(user.roles, ['ROLE_SHOW_ACCEPTED_MINIONS', 'ROLE_ROOT']) ?
                <MinionsAccepted acceptedMinions={this.props.minions.acceptedMinions} messages={messages}
                                 deleteMinions={deleteMinions} deleteMinionsError={deleteMinionsError}
                                 deleteMinionsSuccess={deleteMinionsSuccess}
                                 setDeletedFalse={::this.setDeletedFalse}
                                 setDeleteErrorFalse={::this.setDeleteErrorFalse}
                                 user={user}/> : null,

            unacceptedMinions = containsRole(user.roles, ['ROLE_SHOW_UNACCEPTED_MINIONS', 'ROLE_ROOT']) ?
                <MinionsUnaccepted unacceptedMinions={this.props.minions.unacceptedMinions}
                                   getMinionsGroups={getMinionsGroups} messages={messages}
                                   minionsGroups={this.props.minionsGroups.groups}
                                   acceptMinions={acceptMinions}
                                   acceptMinionsSuccess={acceptMinionsSuccess}
                                   rejectMinions={rejectMinions}
                                   rejectMinionsSuccess={rejectMinionsSuccess}
                                   setRejectedFalse={::this.setRejectedFalse}
                                   user={user}/> : null,

            deniedMinions = containsRole(user.roles, ['ROLE_SHOW_DENIED_MINIONS', 'ROLE_ROOT']) ?
                <MinionsDenied deniedMinions={this.props.minions.deniedMinions} messages={messages}
                               deleteMinions={deleteMinions}
                               deleteMinionsSuccess={deleteMinionsSuccess}
                               setDeletedFalse={::this.setDeletedFalse}/> : null,

            rejectedMinions = containsRole(user.roles, ['ROLE_SHOW_REJECTED_MINIONS', 'ROLE_ROOT']) ?
                <MinionsRejected rejectedMinions={this.props.minions.rejectedMinions}
                                 deleteMinions={deleteMinions} messages={messages}
                                 deleteMinionsSuccess={deleteMinionsSuccess}
                                 setDeletedFalse={::this.setDeletedFalse}
                                 user={user}/> : null;

        return <div className='wrapper'>
            <Header header={messages['client.header.minions.title']} messages={messages}
                    setLanguage={::this.setLanguage}/>
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='3' xs='12' lg='3'>
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
                        <Col md='9' xs='12' lg='9'>
                            <Tabs className='minions-tabs' justified={true}>

                                {containsRole(user.roles, ['ROLE_SHOW_ACCEPTED_MINIONS', 'ROLE_ROOT']) ?
                                    <Tab className='minions-tabs'
                                         label={messages['client.minions.state.accepted']}>{acceptedMinions}</Tab> : null}

                                {containsRole(user.roles, ['ROLE_SHOW_UNACCEPTED_MINIONS', 'ROLE_ROOT']) ?
                                    <Tab className='minions-tabs'
                                         label={messages['client.minions.state.unaccepted']}>{unacceptedMinions}</Tab> : null}

                                {containsRole(user.roles, ['ROLE_SHOW_REJECTED_MINIONS', 'ROLE_ROOT']) ?
                                    <Tab className='minions-tabs'
                                         label={messages['client.minions.state.rejected']}>{rejectedMinions}</Tab> : null}

                                {containsRole(user.roles, ['ROLE_SHOW_DENIED_MINIONS', 'ROLE_ROOT']) ?
                                    <Tab className='minions-tabs'
                                         label={messages['client.minions.state.denied']}>{deniedMinions}</Tab> : null}
                            </Tabs>
                        </Col>
                    </Row>
                </Container>
            </main>
        </div>
    }
}

function mapStateToProps(state) {
    return {
        minions: state.minions,
        minionsGroups: state.minionsGroups,
        acceptMinions: state.acceptMinions,
        rejectMinions: state.rejectMinions,
        deleteMinions: state.deleteMinions,
        localization: state.localization,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        minionsAction: bindActionCreators(minionsAction, dispatch),
        getMinionsGroupsAction: bindActionCreators(getMinionsGroupsAction, dispatch),
        acceptMinionsAction: bindActionCreators(acceptMinionsAction, dispatch),
        rejectMinionsAction: bindActionCreators(rejectMinionsAction, dispatch),
        deleteMinionsAction: bindActionCreators(deleteMinionsAction, dispatch),
        getMessagesAction: bindActionCreators(getMessagesAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Minions)