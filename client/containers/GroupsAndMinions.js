import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Container from 'muicss/lib/react/container';
import getGroupedMinions from '../actions/GetGroupedMinionsAction';

class GroupsAndMinions extends Component {

    componentDidMount() {

    }

    render() {

        return <div className='wrapper'>
            <Header />
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='3' xs='6' lg='3'>
                            Текст 1
                        </Col>
                        <Col md='9' xs='6' lg='9'>
                            Текст 2
                        </Col>
                    </Row>
                </Container>
            </main>
        </div>
    }
}

function mapStateToProps(state) {
    return {
        groupedMinions: state.groupedMinions
    }
}

function mapDispatchToProps(dispatch) {
    return {
        getGroupedMinions: bindActionCreators(getGroupedMinions, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(GroupsAndMinions)