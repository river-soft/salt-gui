import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Input from 'muicss/lib/react/input';
import Container from 'muicss/lib/react/container';
import * as getGroupedMinionsAction from '../actions/GetGroupedMinionsAction';
import * as minionDetailsAction from '../actions/MinionDetailsAction';
import TreeView from '../components/tree/TreeView';

class GroupsAndMinions extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterMinions: [],
            rerender: false
        }
    }

    componentDidMount() {
        const {getGroupedMinions} = this.props.getGroupedMinionsAction;
        if(typeof getGroupedMinions === 'function') {
            getGroupedMinions();
        }
    }

    filterTree(e) {
        let obj = [],
            groupedMinions = this.props.groupedMinions.groupedMinions;

        for (let i = 0; i < groupedMinions.length; i++) {

            let minions = groupedMinions[i].minions.filter((item) => {
                return item.name.toLowerCase().search(e.target.value.toLowerCase()) !== -1
            });

            if (minions.length > 0) {
                obj.push({
                    group: groupedMinions[i].group,
                    minions: minions
                })
            }
        }

        this.setState({
            filterMinions: obj,
            rerender: true
        });
    }

    render() {

        const {getMinionDetails} = this.props.minionDetailsAction;

        let treeView;

        if (this.props.groupedMinions.groupedMinions.length === 0) {
            treeView = <div>Данных нету</div>
        } else if (this.state.rerender) {
            treeView = <TreeView groups={this.state.filterMinions} showContent={getMinionDetails}/>;
        } else {
            treeView = <TreeView groups={this.props.groupedMinions.groupedMinions} showContent={getMinionDetails}/>;
        }

        return <div className='wrapper'>
            <Header />
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='3' xs='6' lg='3'>
                            <Input label='Поиск' floatingLabel={true} onChange={e => {this.filterTree(e)}}/>
                            <ul className='list mui-list--unstyled'>
                                {treeView}
                            </ul>
                            <button className='mui-btn button'>добавить</button>
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
        groupedMinions: state.groupedMinions,
        minionDetails: state.minionDetails
    }
}

function mapDispatchToProps(dispatch) {
    return {
        getGroupedMinionsAction: bindActionCreators(getGroupedMinionsAction, dispatch),
        minionDetailsAction: bindActionCreators(minionDetailsAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(GroupsAndMinions)