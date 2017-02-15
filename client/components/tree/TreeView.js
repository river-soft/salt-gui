import React, {Component} from 'react';
import TreeNode from '../tree/TreeNode';

export default class TreeView extends Component {

    render() {
        let groups = this.props.groups;
        let nodes = groups.length ? groups.map((group, index) => <TreeNode group={group} key={index}
                                                                           removeGroup={this.props.removeGroup}
                                                                           nodes={group.scripts || group.minions}
                                                                           showContent={this.props.showContent}/>) : null;

        return <div>{nodes}</div>
    }
}