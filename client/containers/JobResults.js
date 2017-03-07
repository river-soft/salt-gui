import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import Container from 'muicss/lib/react/container';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import JobResultCounters from '../components/jobResults/JobResultConters';
import * as jobResultsAction from '../actions/JobResultsAction';


class JobResults extends Component {

    constructor(props) {
        super(props);

        this.state = {
            client: ''
        }
    }

    componentDidMount() {
        const {jobResults} = this.props.jobResultsAction;

        let client = jobResults();

        this.setState({
            client: client
        })
    }

    componentDidUpdate() {
    }

    render() {

        return <div className='wrapper'>
            <Header />
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='4' xs='6' lg='4'>
                            <h4>Результаты выполнения скриптов</h4>
                            <JobResultCounters/>
                        </Col>
                        <Col md='8' xs='6' lg='8'>
                            dva
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