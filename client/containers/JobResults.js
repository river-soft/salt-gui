import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import Container from 'muicss/lib/react/container';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import JobResultCounters from '../components/jobResults/JobResultCounters';
import * as jobResultsAction from '../actions/JobResultsAction';
import Tabs from 'muicss/lib/react/tabs';
import Tab from 'muicss/lib/react/tab';


class JobResults extends Component {

    constructor(props) {
        super(props);

        this.state = {
            client: '',
            showJobDetails: false
        }
    }

    componentDidMount() {
        const {jobResults} = this.props.jobResultsAction;

        let client = jobResults();

        this.setState({
            client: client
        })
    }

    componentWillUnmount() {
        this.state.client.disconnect();
    }

    showJobDetails(jid) {
        this.setState({showJobDetails: true});
        this.state.client.send('/request/find-all-results-by-job', {}, JSON.stringify({jid: jid}));
        this.state.client.subscribe('/queue/job-results/update-all-results-by-job', (obj) => {

        });
    }


    render() {

        return <div className='wrapper'>
            <Header />
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='4' xs='6' lg='4'>
                            <h4>Результаты выполнения скриптов</h4>
                            <JobResultCounters jobResults={this.props.jobResults.result}
                                               showJobDetails={::this.showJobDetails}/>
                        </Col>
                        <Col md='8' xs='6' lg='8'>
                            {this.state.showJobDetails ?
                                <Tabs className='minions-tabs' justified={true}>
                                    <Tab className='minions-tabs' label='All'>All</Tab>
                                    <Tab className='minions-tabs' label='True'>True</Tab>
                                    <Tab className='minions-tabs' label='False'>False</Tab>
                                    <Tab className='minions-tabs' label='No connect'>No connect</Tab>
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