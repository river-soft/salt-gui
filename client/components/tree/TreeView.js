import React, {Component} from 'react';
import TreeNode from '../tree/TreeNode';

export default class TreeView extends Component {

    constructor(props) {
        super(props);

        this.state = {
            selected: ''
        }
    }

    setSelected(filter) {
        this.setState({selected: filter});
    }

    render() {

        let groups = this.props.groups;
        let nodes = groups.length ? groups.map((group, index) => <TreeNode group={group} key={index}
                                                                           editGroup={this.props.editGroup}
                                                                           removeGroup={this.props.removeGroup}
                                                                           nodes={group.scripts || group.minions}
                                                                           showContent={this.props.showContent}
                                                                           removeIfNotEmpty={this.props.removeIfNotEmpty}
                                                                           rerender={this.props.rerender}
                                                                           createdGroup={this.props.createdGroup}
                                                                           setSelected={::this.setSelected}
                                                                           selected={this.state.selected}/>) : null;

        return <div>{nodes}</div>
    }
}