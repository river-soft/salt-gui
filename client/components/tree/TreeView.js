import React, {Component} from 'react';
import TreeNode from '../tree/TreeNode';

export default class TreeView extends Component {

    render() {
        let groups = this.props.groups;
        let nodes = groups.map((group, index) => <TreeNode group={group} files={group.files} key={index} showContent={this.props.showContent} />);

        return <div>{nodes}</div>
    }
}