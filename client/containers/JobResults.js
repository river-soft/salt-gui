import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import Container from 'muicss/lib/react/container';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import JobResultCounters from '../components/jobResults/JobResultCounters';
import JobAllResults from '../components/jobResults/JobAllResults';
import * as jobResultsAction from '../actions/JobResultsAction';
import Tabs from 'muicss/lib/react/tabs';
import Tab from 'muicss/lib/react/tab';

class JobResults extends Component {

    constructor(props) {
        super(props);

        this.state = {
            client: '',
            showJobDetails: false,
            getJobResults: ''
        }
    }

    componentDidMount() {
        const {jobResults} = this.props.jobResultsAction;

        let obj = jobResults();

        this.setState({
            client: obj.client,
            getJobResults: obj.getJobResults
        })
    }

    componentWillUnmount() {
        this.state.client.disconnect();
    }

    showJobScriptResults(jid) {
        try {
            this.state.getJobResults(jid);
            this.setState({showJobDetails: true});
        } catch (error) {
            new Error(error);
        }
    }


    render() {

        let jobResults = this.props.jobResults.jobScriptResults, trueResults = [], falseResults = [], noConnectResults = [];

        for (let i = 0; i < jobResults.length; i++) {
            if(jobResults[i].status === 'true') {
                trueResults.push(jobResults[i]);
            } else if (jobResults[i].status === 'false') {
                falseResults.push(jobResults[i]);
            } else if (jobResults[i].status === 'no connected') {
                noConnectResults.push(jobResults[i]);
            } else {
                console.error(new Error(`Job result has no register status: ${jobResults[i].status}`));
            }
        }

        return <div className='wrapper'>
            <Header />
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='4' xs='6' lg='4'>
                            <h4>Результаты выполнения скриптов</h4>
                            <JobResultCounters jobResults={this.props.jobResults.result}
                                               showJobScriptResults={::this.showJobScriptResults}/>
                        </Col>
                        <Col md='8' xs='6' lg='8'>
                            {this.state.showJobDetails ?
                                <Tabs className='minions-tabs' justified={true}>
                                    <Tab className='minions-tabs' label='All'><JobAllResults jobResults={jobResults} showStatus={true}/></Tab>
                                    <Tab className='minions-tabs' label='True'><JobAllResults jobResults={trueResults}/></Tab>
                                    <Tab className='minions-tabs' label='False'><JobAllResults jobResults={falseResults}/></Tab>
                                    <Tab className='minions-tabs' label='No connect'><JobAllResults jobResults={noConnectResults}/></Tab>
                                </Tabs> : null}
                        </Col>
                    </Row>
                </Container>
            </main>
        </div>
    }
}

function mapStateToProps(state) {
    return {
        jobResults: state.jobResults
    }
}

function mapDispatchToProps(dispatch) {
    return {
        jobResultsAction: bindActionCreators(jobResultsAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(JobResults)