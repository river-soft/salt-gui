import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import Container from 'muicss/lib/react/container';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import JobResultCounters from '../components/jobResults/JobResultCounters';
import JobAllResults from '../components/jobResults/JobAllResults';
import JobResultDetails from '../components/jobResults/JobResultDetails';
import DateForSelect from '../components/DateForSelect';
import * as jobResultsAction from '../actions/JobResultsAction';
import * as jobDetailsAction from '../actions/JobDetailsAction';
import * as reExecuteScriptsAction from '../actions/ReExecuteScripts';
import Tabs from 'muicss/lib/react/tabs';
import Tab from 'muicss/lib/react/tab';

class JobResults extends Component {

    constructor(props) {
        super(props);

        this.state = {
            client: '',
            showJobDetails: false,
            getJobResults: '',
            unSubscribeJobResults: '',
            subscription: '',
            clearFilter: false,
            showJobDescription: false,
            jobResult: '',
            scriptsName: '',
            clearCheckedList: false,
            load : ''
        }
    }

    componentDidMount() {
        const {jobResults} = this.props.jobResultsAction;

        let obj = jobResults();

        this.setState({
            client: obj.client,
            getJobResults: obj.getJobResults,
            unSubscribeJobResults: obj.unSubscribeJobResults,
            load : obj.load
        })
    }

    componentWillUnmount() {
        this.state.client.disconnect();
    }

    showJobScriptResults(jid, script) {
        try {

            let scripts = script.split('/');

            if (this.state.subscription) {
                this.state.unSubscribeJobResults(this.state.subscription);
            }

            let subscription = this.state.getJobResults(jid);
            this.setState({
                showJobDetails: true,
                subscription: subscription,
                showJobDescription: false,
                scriptsName: scripts
            });
        } catch (error) {
            new Error(error);
        }
    }

    hideJobScriptsResult() {
        this.state.unSubscribeJobResults(this.state.subscription);
        this.setState({showJobDetails: false});
    }

    clearFilter() {
        this.setState({clearFilter: true});
    }

    clearFilterFalse() {
        this.setState({clearFilter: false});
    }

    showJobDetails(jobResult) {

        const {jobDetails} = this.props.jobDetailsAction;
        jobDetails(jobResult.id);

        this.setState({
            showJobDescription: true,
            showJobDetails: false,
            jobResult: jobResult
        });
    }

    returnFromJobDetails() {
        this.setState({
            showJobDescription: false,
            showJobDetails: true,
        });
    }

    setExecuteFalse() {
        this.props.reExecuteScripts.reExecute = false;
    }

    setExecuteErrorFalse() {
        this.props.reExecuteScripts.error = false;
    }

    clearCheckedList() {
        this.setState({clearCheckedList: true});
    }

    clearCheckedListFalse() {
        this.setState({clearCheckedList: false});
    }

    render() {

        const {reExecuteScripts} = this.props.reExecuteScriptsAction;

        let jobResults = this.props.jobResults.jobScriptResults, trueResults = [], falseResults = [], noConnectResults = [],
            executeError = this.props.reExecuteScripts.error, resultDetails = this.props.jobDetails.jobDetails,
            executeSuccess = this.props.reExecuteScripts.reExecute;

        for (let i = 0; i < jobResults.length; i++) {
            if (jobResults[i].status === 'true') {
                trueResults.push(jobResults[i]);
            } else if (jobResults[i].status === 'false') {
                falseResults.push(jobResults[i]);
            } else if (jobResults[i].status === 'no connected') {
                noConnectResults.push(jobResults[i]);
            } else if (jobResults[i].status === 'waiting') {
                noConnectResults.push(jobResults[i]);
            } else {
                console.error(new Error(`Job result has no register status: ${jobResults[i].status}`));
            }
        }

        return <div className='wrapper'>
            <Header header='Результаты выполнения скриптов'/>
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='4' xs='12' lg='4'>
                            <DateForSelect loadData={this.state.load}/>
                            <JobResultCounters jobResults={this.props.jobResults.result}
                                               showJobScriptResults={::this.showJobScriptResults}
                                               hideJobScriptsResult={::this.hideJobScriptsResult}
                                               clearFilter={::this.clearFilter}
                                               setExecuteFalse={::this.setExecuteFalse}
                                               clearCheckedList={::this.clearCheckedList}
                                               setExecuteErrorFalse={::this.setExecuteErrorFalse}/>
                        </Col>
                        <Col md='8' xs='12' lg='8'>
                            {this.state.showJobDetails ?
                                <Tabs className='minions-tabs' justified={true}>
                                    <Tab className='minions-tabs' label='Все'>
                                        <JobAllResults jobResults={jobResults} showStatus={true}
                                                       clearFilter={this.state.clearFilter}
                                                       clearFilterFalse={::this.clearFilterFalse}
                                                       resultDetails={resultDetails}
                                                       showJobDetails={::this.showJobDetails}/></Tab>
                                    <Tab className='minions-tabs' label='Выполнено'><JobAllResults
                                        jobResults={trueResults}
                                        clearFilter={this.state.clearFilter}
                                        clearFilterFalse={::this.clearFilterFalse}
                                        resultDetails={resultDetails}
                                        showJobDetails={::this.showJobDetails}/></Tab>
                                    <Tab className='minions-tabs' label='Не выполнено'><JobAllResults
                                        jobResults={falseResults} clearFilter={this.state.clearFilter}
                                        clearFilterFalse={::this.clearFilterFalse} showSelect={true}
                                        resultDetails={resultDetails} showJobDetails={::this.showJobDetails}
                                        reExecuteScripts={reExecuteScripts} executeError={executeError}
                                        setExecuteErrorFalse={::this.setExecuteErrorFalse}
                                        scriptName={this.state.scriptsName} executeSuccess={executeSuccess}
                                        setExecuteFalse={::this.setExecuteFalse}
                                        clearCheckedListFalse={::this.clearCheckedListFalse}
                                        clearCheckedList={this.state.clearCheckedList}/></Tab>
                                    <Tab className='minions-tabs' label='Нет соединения'><JobAllResults
                                        jobResults={noConnectResults} clearFilter={this.state.clearFilter}
                                        clearFilterFalse={::this.clearFilterFalse} showSelect={true}
                                        resultDetails={resultDetails} showJobDetails={::this.showJobDetails}
                                        reExecuteScripts={reExecuteScripts} executeError={executeError}
                                        setExecuteErrorFalse={::this.setExecuteErrorFalse}
                                        scriptName={this.state.scriptsName} executeSuccess={executeSuccess}
                                        setExecuteFalse={::this.setExecuteFalse}
                                        clearCheckedListFalse={::this.clearCheckedListFalse}
                                        clearCheckedList={this.state.clearCheckedList}/></Tab>
                                </Tabs> : null}
                            {this.state.showJobDescription ?
                                <div className='job-results'>
                                    <h4 className='job-results__header'>Результаты выполнения скриптов для миньона
                                        <strong> {this.state.jobResult.minionName}</strong>
                                        <span className='arrow-back' onClick={::this.returnFromJobDetails}><i
                                            className='mi mi-keyboard-backspace'></i>Назад</span>
                                    </h4>
                                    {resultDetails.length ? resultDetails.map((result, i) => {
                                            return <JobResultDetails result={result} key={i}/>
                                        }) : <p>Результатов нет</p>}
                                </div> : null}
                        </Col>
                    </Row>
                </Container>
            </main>
        </div>
    }
}

function mapStateToProps(state) {
    return {
        jobResults: state.jobResults,
        jobDetails: state.jobDetails,
        reExecuteScripts: state.reExecuteScripts
    }
}

function mapDispatchToProps(dispatch) {
    return {
        jobResultsAction: bindActionCreators(jobResultsAction, dispatch),
        jobDetailsAction: bindActionCreators(jobDetailsAction, dispatch),
        reExecuteScriptsAction: bindActionCreators(reExecuteScriptsAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(JobResults)